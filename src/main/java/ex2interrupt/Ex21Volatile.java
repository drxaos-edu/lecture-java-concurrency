package ex2interrupt;

class Incremenator1 extends Thread {

    private volatile boolean mFinish = false;

    public void finish()        //Инициирует завершение потока
    {
        mFinish = true;
    }

    @Override
    public void run() {
        do {
            if (!mFinish)    //Проверка на необходимость завершения
            {
                Ex21Volatile.mValue++;    //Инкремент

                //Вывод текущего значения переменной
                System.out.print(Ex21Volatile.mValue + " ");
            } else {
                return;        //Завершение потока
            }

            try {
                Thread.sleep(500);        //Приостановка потока на 1 сек.
            } catch (InterruptedException e) {
            }
        }
        while (true);
    }
}

public class Ex21Volatile {

    //Переменая, которой оперирует инкременатор
    public static int mValue = 0;

    static Incremenator1 mInc;    //Объект побочного потока

    public static void main(String[] args) throws InterruptedException {
        mInc = new Incremenator1();    //Создание потока

        System.out.print("Значение = ");

        mInc.start();    //Запуск потока

        Thread.sleep(3 * 1000); //Ожидание в течении 3 сек.

        mInc.finish();    //Инициация завершения побочного потока
    }
}
