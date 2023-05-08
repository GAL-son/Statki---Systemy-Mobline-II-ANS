package com.battleships.model.client;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.battleships.model.client.board.Field;
import com.battleships.model.client.players.Player;
import com.battleships.model.client.players.PlayerAi;
import com.battleships.model.client.players.PlayerLocal;
import com.battleships.model.client.players.PlayerRemote;
import com.battleships.model.client.ship.Ship;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {

    private int id;
    private int state;
    // for now 0-in progress ship placing, 1-in progress shooting , 2- ended
    private int statePlayer1 = 0;
    private int statePlayer2 = 0;
    private int type;
    // for now -> with 0 - with ai, 1 - online
    private int turn;
    private int player_active;
    private Player player1;
    private Player player2;


    public Game(int id, int type) throws Exception {
        //seting stater to in progres, ship placing
        this.state = 0;
        this.turn = 0;
        this.player_active = 0;
        //0 would mean that no player is active, as in phase of palcing ships, both can moove


        //recoginisng game mode
        System.out.println("witaj w grze w statki");
        this.id = id;
        if (type >= 0 && type <= 1) {
            this.type = type;
        } else {
            this.state = 2;
            throw new Exception("type specified not recoginised");
        }

        this.player1 = new PlayerLocal();

        //seting up right players
        switch (this.type) {
            case 0:
                this.player2 = new PlayerAi();
                break;
            case 1:
                this.player2 = new PlayerRemote();
                break;
            default:
                this.state = 2;
                throw new Exception("type specified not recoginised");
        }

        //specifing ships sizes
        for (int i = 1; i < 5; i++)
            for (int x = 0; x < i; x++) {
                player1.shipsSizes.add(5 - i);
                player2.shipsSizes.add(5 - i);
            }

        Log.i("test", "game creation ");


    }

    public void getMove(Move move, int player) {
        if (this.state == 0) {
            Log.i("debug", "placing ship for player" + String.valueOf(player));
            place_ship(move, player, 0);
        }
        if (this.state == 1) {

        }
    }


    public void place_ship(Move move, int player, int aline) {
        //WYMYUŚLIĆ SKĄD MA BYĆ BRANY ALIGNMENT

        //wybiramy gracza ktory ruszyl
        Player currentPlayer;
        if (player == 1)
            currentPlayer = this.player1;
        else if (player == 2) {
            currentPlayer = this.player2;
        } else {
            return;
        }


        //jesli gracz ktury rusza nie ma juz statkuw do psotawienia zakoncz wykonywanie
        if (currentPlayer.shipsSizes.isEmpty()) {
            Log.i("debug", "all placed for player  " + String.valueOf(player));
            return;
        }

        //pobiez  rozmiar nastepnego statku
        int size = currentPlayer.shipsSizes.get(currentPlayer.shipsSizes.size() - 1);

        if (aline == 0) {
            Log.i("debug", "checking orizontal " + String.valueOf(move.positionX) + "+" + String.valueOf(size));
            //sprawdz czy mozna postawic statek w danym miejscu (beta) jesli nie to zakoncz wykonywanie
            if (move.positionX + (size - 1) > 9) {
                Log.i("debug", "statek nie moze byc stwozony horizontal ");
                return;
            }
        }
        if (aline == 1) {
            Log.i("debug", "checking veritcal");
            //sprawdz czy mozna postawic statek w danym miejscu (beta) jesli nie to zakoncz wykonywanie // konieczie POPRAWIĆ
            if (move.positionY + (size - 1) > 9) {
                Log.i("debug", "statek nie moze byc stwozony vertical" + String.valueOf(statePlayer2));
                return;
            }
        }

        boolean pom_return = false;
        //sprawdzenie czy statek dotyka się z innym statkiem

        if (aline == 0) {
            for (int n = move.positionX - 1; n < move.positionX + size + 1; n++) {
                if (n >= 0 && n <= 9) {
                    if (n == move.positionX - 1 || n == move.positionX + size) {

                        if (((Field) currentPlayer.getPlayerBard().fields.get(n).get(move.positionY)).getOocupyingShip() != null) {
                            Log.i("debug", "znaleziono styk");
                            pom_return = true;
                        }

                    }

                    if (move.positionY + 1 < 10) {
                        if (((Field) currentPlayer.getPlayerBard().fields.get(n).get(move.positionY + 1)).getOocupyingShip() != null) {
                            Log.i("debug", "znaleziono styk");
                            pom_return = true;
                        }
                    }

                    if (move.positionY - 1 > -1) {
                        if (((Field) currentPlayer.getPlayerBard().fields.get(n).get(move.positionY - 1)).getOocupyingShip() != null) {
                            Log.i("debug", "znaleziono styk");
                            pom_return = true;
                        }

                    }
                }


            }
        }


        if (aline == 1) {
            for (int n = move.positionY - 1; n < move.positionY + size + 1; n++) {
                if (n >= 0 && n <= 9) {
                    if (n == move.positionY - 1 || n == move.positionY + size) {

                        if (((Field) currentPlayer.getPlayerBard().fields.get(move.positionX).get(n)).getOocupyingShip() != null) {
                            Log.i("debug", "znaleziono styk");
                            pom_return = true;
                        }

                    }

                    if (move.positionX + 1 < 10) {
                        if (((Field) currentPlayer.getPlayerBard().fields.get(move.positionX + 1).get(n)).getOocupyingShip() != null) {
                            Log.i("debug", "znaleziono styk");
                            pom_return = true;
                        }
                    }

                    if (move.positionX - 1 > -1) {
                        if (((Field) currentPlayer.getPlayerBard().fields.get(move.positionX - 1).get(n)).getOocupyingShip() != null) {
                            Log.i("debug", "znaleziono styk");
                            pom_return = true;
                        }

                    }
                }


            }
        }
        if (pom_return == true)
            return;

        //dodaj statek do listy statkow gracza
        Log.i("debug", "statek dodany do listy" + String.valueOf(player));
        currentPlayer.ships.add(new Ship(size));

        //powiąz statek z polami na planszy gracza(dodac)
        for (int i = 0; i < size; i++) {
            Log.i("debug", "powioązano" + String.valueOf(i) + " z " + String.valueOf(size));
            try {

                Log.i("xd?", "statek dowiazany na polu" + String.valueOf(move.positionX) + " ," + String.valueOf(move.positionY));
                if (aline == 0)
                    ((Field) currentPlayer.getPlayerBard().fields.get(move.positionX + i).get(move.positionY)).setOocupyingShip(currentPlayer.ships.get(currentPlayer.ships.size() - 1));
                if (aline == 1)
                    ((Field) currentPlayer.getPlayerBard().fields.get(move.positionX).get(move.positionY + i)).setOocupyingShip(currentPlayer.ships.get(currentPlayer.ships.size() - 1));
            } catch (Exception e) {
                Log.i("wtf", e.getMessage());
            }
        }
        //usun rozmair stwozonego statku
        Log.i("debug", "lista statkow do stworzenia zmniejszona");
        currentPlayer.shipsSizes.remove(currentPlayer.shipsSizes.size() - 1);

        //sprawdz stan rozgrywki dla gracza
        if (currentPlayer.shipsSizes.isEmpty()) {
            if (player == 1) {
                statePlayer1 = 1;
            } else if (player == 2) {
                statePlayer2 = 1;
            }
        }
        Log.i("debug", "status rozgrywki dla gracza1:" + String.valueOf(statePlayer1));
        Log.i("debug", "status rozgrywki dla gracza2:" + String.valueOf(statePlayer2));

        //jesli  obaj gracze są juz w  stanie rozgrywki, ustaw stan rozgrywki na nastepny i zakoncz wykonyewanie
        if (statePlayer1 == 1 && statePlayer2 == 1) {
            state = 1;
        }
        Log.i("debug", "status rozgrywki :" + String.valueOf(state));
        //przypisz tymczasowy obiekt do odpowiedniego gracza
        if (player == 1)
            this.player1 = currentPlayer;
        else if (player == 2) {
            this.player2 = currentPlayer;
        }

    }


    public void gameLoop() {

    }

    public int getId() {
        return id;
    }

    public int getState() {
        return state;
    }

    public int getType() {
        return type;
    }

    public int getTurn() {
        return turn;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public static ArrayList<Integer> histogram(ArrayList<Integer> a) {
        ArrayList<Integer> histogram = new ArrayList<>();
        for (int n = 0; n < 4; n++) {
            histogram.add(0);
        }
        histogram.add(0);
        for (Integer x : a) {
            histogram.set(x - 1, (histogram.get(x - 1) + 1));
        }
        Log.i("histogram", "histogram: jeden*" + String.valueOf(histogram.get(0)) + " dwa*" + String.valueOf(histogram.get(1)) + " trzy*" + String.valueOf(histogram.get(2)) + " cztery*" + String.valueOf(histogram.get(3)));
        return histogram;
    }

}


