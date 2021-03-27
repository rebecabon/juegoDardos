package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;

public class Main extends Application {

    /*
     * PROBLEMA
     * elabora un programa que simule el juego de dardos
     * genera el tablero y lanza dardos de manera aleatoria (max 1,000,000)
     * con la formula adecuada, muestra por consola el valor aproximado de pi
     * SOLUCION
     * aplicacion con libreria javafx
     */

    public ArrayList<Circle> darts = new ArrayList<Circle>(); // conjunto de dardos
    public ArrayList<Circle> wins = new ArrayList<Circle>(); // dardos dentro del circulo
    public ArrayList<Circle> fails = new ArrayList<Circle>(); // dardos fuera del circulo
    public Group content = new Group(); // contenido del plano
    public Random randy = new Random(); // generador de numeros aleatorios
    public static int numDarts; // dardos a lanzar

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("SIMULACION - EQUIPO 2");
        VBox layout = new VBox(2); // contenedor
        unitCircle(); // generar circulo unitario
        dartGenerator(numDarts); // generar dardos
        layout.getChildren().add(content); // agregar elementos al contenedor (plano)
        results(); // mostrar resultados
        stage.setScene(new Scene(layout, 405, 405, Color.BLACK)); // ventana de (405)(405)
        stage.setResizable(false);
        stage.show();
    }

    public void unitCircle() {
        // crear circulo unitario
        Circle unitCircle = new Circle(); // circulo unitario
        // centrar circulo
        unitCircle.setCenterX(200);
        unitCircle.setCenterY(200);
        // radio a escala 1:200
        unitCircle.setRadius(200);
        unitCircle.setFill(Color.WHITE);
        // dibujar los ejes divisores del circulo unitario
        Line axisX = new Line(200, 0, 200, 400);
        axisX.setStroke(Color.BLACK);
        Line axisY = new Line(0, 200, 400, 200);
        axisY.setStroke(Color.BLACK);
        content.getChildren().addAll(unitCircle, axisX, axisY);
    }

    public void dartGenerator(int numDarts) {
        // crear dardos
        for (int i = 0; i < numDarts; i++) {
            darts.add(new Circle());
            // parte decimal + parte entera
            darts.get(i).setCenterX(randy.nextDouble() + randy.nextInt(400));
            darts.get(i).setCenterY(randy.nextDouble() + randy.nextInt(400));
            darts.get(i).setRadius(2);
            // calcular hipotenusa de los dardos
            boolean inside = hypotenuse(darts.get(i));
            // comprobar si el dardo esta dentro o fuera del circulo
            if (inside) {
                darts.get(i).setFill(Color.RED);
                wins.add(darts.get(i));
            } else {
                darts.get(i).setFill(Color.WHITE);
                fails.add(darts.get(i));
            }
            // agregar dardo al contenido por desplegar en el plano
            content.getChildren().add(darts.get(i));
        }
    }

    public boolean hypotenuse(Circle dart) {
        double catetoX = 0, catetoY = 0, hip;
        // determinar en cual cuadrante se encuentra el dardo
        // para recalcular los catetos que forman el triangulo de sus coordenadas
        // TRABAJADO EN ESCALA 1:200
        // ORIGEN
        if (dart.getCenterX() == 200 && dart.getCenterY() == 200) {
            catetoX = dart.getCenterX();
            catetoY = dart.getCenterY();
            // CUADRANTE 1
        } else if (dart.getCenterX() <= 200 && dart.getCenterY() <= 200) {
            catetoX = 200 - dart.getCenterX();
            catetoY = 200 - dart.getCenterY();
            // CUADRANTE 2
        } else if (dart.getCenterX() >= 200 && dart.getCenterY() >= 200) {
            catetoX = dart.getCenterX() - 200;
            catetoY = dart.getCenterY() - 200;
            // CUADRANTE 3
        } else if (dart.getCenterX() >= 200 && dart.getCenterY() <= 200) {
            catetoX = dart.getCenterX() - 200;
            catetoY = 200 - dart.getCenterY();
            // CUADRANTE 4
        } else if (dart.getCenterX() <= 200 && dart.getCenterY() >= 200) {
            catetoX = 200 - dart.getCenterX();
            catetoY = dart.getCenterY() - 200;
        }
        // a partir de los valores calculados de catetos,
        // calcular hipotenusa
        hip = Math.sqrt((catetoX * catetoX + catetoY * catetoY));
        if (hip <= 200)
            return true; // dentro del circulo
        else
            return false; // fuera del circulo
    }

    public double approximatePI() {
        // aprox_pi = (exitos / intentos) * 4
        double success = wins.size(), trys = darts.size();
        return (success/trys)*4;
    }

    public void results(){
        String leftAlignFormat = "| %-16s | %-25s | %-25s |%n";
        // imprimir tabla de coordenadas dentro del circulo
        System.out.println("\n***DENTRO DEL CIRCULO***");
        System.out.format("+------------------+--------------------------+-----------------------------+%n");
        System.out.format("| DARDO N           | 1:200                    | 1:1                       |%n");
        System.out.format("+------------------+--------------------------+-----------------------------+%n");
        for (int j = 0; j < wins.size(); j++) {
            System.out.format(leftAlignFormat,
                    "DARDO " + (j + 1),
                    // ESCALA 1:200
                    "(" + String.format("%.6f", wins.get(j).getCenterX()) + ", " + String.format("%.6f", wins.get(j).getCenterY()) + ")",
                    // ESCALA 1:1
                    "(" + String.format("%.6f", (wins.get(j).getCenterX()/200)) + ", " + String.format("%.6f", (wins.get(j).getCenterY()/200)) + ")");
        }
        System.out.format("+------------------+---------------------------+----------------------------+%n");
        // imprimir tabla de coordenadas fuera del circulo
        System.out.println("\n***FUERA DEL CIRCULO***");
        System.out.format("+------------------+---------------------------+----------------------------+%n");
        System.out.format("| DARDO N           | 1:200                    | 1:1                       |%n");
        System.out.format("+------------------+---------------------------+----------------------------+%n");
        for (int k = 0; k < fails.size(); k++) {
            System.out.format(leftAlignFormat,
                    "DARDO " + (k + 1),
                    // ESCALA 1:200
                    "(" + String.format("%.6f", fails.get(k).getCenterX()) + ", " + String.format("%.6f", fails.get(k).getCenterY()) + ")",
                    // ESCALA 1:1
                    "(" + String.format("%.6f", (fails.get(k).getCenterX()/200)) + ", " + String.format("%.6f", (fails.get(k).getCenterY()/200)) + ")");
        }
        System.out.format("+------------------+---------------------------+---------------------------+%n");
        // imprimir resultados
        System.out.println("\n***RESULTADOS***");
        System.out.println("DARDOS DENTRO DEL CIRCULO = " + wins.size());
        System.out.println("DARDOS FUERA DEL CIRCULO  = " + fails.size());
        System.out.println("APROXIMADO DE PI          = " + approximatePI());
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        do {
            System.out.print("NUM. DE DARDOS A LANZAR (1:1000000) = ");
            numDarts = scan.nextInt();
        } while (numDarts < 1 || numDarts > 1000000);
        launch(args);
    }
}