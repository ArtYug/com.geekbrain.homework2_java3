package com.geekbrains.homework4_java3;

/*     Homework4 java 4
1. Создать три потока, каждый из которых выводит определенную букву (A, B и C)
             5 раз (порядок – ABСABСABС). Используйте wait/notify/notifyAll.
2. На серверной стороне сетевого чата реализовать управление потоками через ExecutorService.
changing class ClientHandler ,Server
*/
public class MainApp {
    public final Object mon = new Object();
    public char letterA = 'A';

    public static void main(String[] args) {
        MainApp mainApp = new MainApp();
        new Thread(mainApp::printA).start();
        new Thread(mainApp::printB).start();
        new Thread(mainApp::printC).start();
    }

    public void printA() {
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (letterA != 'A') {
                        mon.wait();
                    }
                    System.out.print("A");
                    letterA = 'B';
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printB() {
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (letterA != 'B') {
                        mon.wait();
                    }
                    System.out.print("B");
                    letterA = 'C';
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printC() {
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (letterA != 'C') {
                        mon.wait();
                    }
                    System.out.print("C");
                    letterA = 'A';
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
