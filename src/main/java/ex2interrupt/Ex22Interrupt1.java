package ex2interrupt;

class Incremenator2 extends Thread {

    @Override
    public void run() {
        do {
            if (!Thread.interrupted())    //Проверка прерывания
            {
                Ex22Interrupt1.mValue++;    //Инкремент

                //Вывод текущего значения переменной
                System.out.print(Ex22Interrupt1.mValue + " ");
            } else {
                return;        //Завершение потока
            }

            try {
                Thread.sleep(500);        //Приостановка потока на 1 сек.
            } catch (InterruptedException e) {
                return;    //Завершение потока после прерывания
            }
        }
        while (true);
    }
}

public class Ex22Interrupt1 {

    //Переменая, которой оперирует инкременатор
    public static int mValue = 0;

    static Incremenator2 mInc;    //Объект побочного потока

    public static void main(String[] args) throws InterruptedException {
        mInc = new Incremenator2();    //Создание потока

        System.out.print("Значение = ");

        mInc.start();    //Запуск потока

        Thread.sleep(3 * 1000);        //Ожидание в течении i*2 сек.

        mInc.interrupt();    //Прерывание побочного потока
    }
}
