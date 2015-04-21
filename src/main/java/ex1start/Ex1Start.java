package ex1start;

class AffableThread extends Thread {
    @Override
    public void run() {
        System.out.println("Привет из побочного потока!");
    }
}

public class Ex1Start {

    static AffableThread mSecondThread;

    public static void main(String[] args) {
        mSecondThread = new AffableThread();    //Создание потока
        mSecondThread.start();                  //Запуск потока

        System.out.println("Главный поток завершён.");
    }

}
