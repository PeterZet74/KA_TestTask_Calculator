import javax.print.attribute.standard.PresentationDirection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.SplittableRandom;
import java.util.regex.Pattern;

/**
 * Created by Zenkov Petr E. (R) 18.11.2022
 *
 * Тестовое задание по курсу Java-разработчик
 * Задание:
 * Создать консольное приложение "КАЛЬКУЛЯТОР"
 * "КАЛЬКУЛЯТОР" должен выполнить арифметические действия
 * над часлами из введенной сроки.
 *
 * Дополнительные условия:
 * - операции выполняются над целыми Арабскими и Римскими
 *   сислами в диапозоне от 0 до 10
 * - желаемое действие передается одной строкой в виде "А + Б"
 * - операнды должны быть одной системы, иначе выход по
 *   Исключению
 * - если указана неверная оперция (не +, -, *, /),
 *   выхоб по Исключению
 * - при делении на 0 (ноль) - Исключение
 * - при вычислении Риских чисел - результат < (меньше) 1 -
 *   исключение
 *
 *  5. Выводим результат.
 */

public class Calculator {
    public static String errorMessage = "";
    public static String errorCode = "";
    public static  boolean errorStatus = false;

    enum RomanNumbers {
        I(1), V(5), X(10), L(50), C(100), D(500), M(1000);

        private int value;

        RomanNumbers (int value){
            this.value = value;
        }

        public int getValue(){
            return value;
        }
    }

    public static int convertRomanToArabic (String Numbers){

        int result = 0;

        for (int i = 0; i < Numbers.length(); i++) {
            //            char c = strNumber.charAt(i);
            //            String symbol = String.valueOf( c );
            int Number = RomanNumbers.valueOf(String.valueOf(Numbers.charAt(i))).getValue();

            if (i + 1 < Numbers.length()) {
                int nextNumber = RomanNumbers.valueOf(String.valueOf(Numbers.charAt(i + 1))).getValue();
                Number = (Number >= nextNumber ? Number : -Number);
            }

            result += Number;
            //         System.out.println( Number );
        }

//            System.out.println("result =" + result);

        return result;
    }

    public static  String convertArabiToRoman (int ArabicNumber ){
        RomanNumbers[] RN = RomanNumbers.values();

        int i = Long.valueOf(Arrays.stream(RN).count()).intValue()-1;
        String result = "";

        while ( ArabicNumber > 0 && i >= 0) {
            String currenRomanNumber = RN[i].name();
            int currentRomanValue = RN[i].getValue();
            int pi = i - 2 >= 0 ? i - 2 : 0;

            if (ArabicNumber > currentRomanValue) {
//                System.out.println(currenRomanNumber + " => " + ArabicNumber);
//                ArabicNumber -= currentRomanValue;
            } else if (ArabicNumber > currentRomanValue - RN[pi].getValue() && pi >=0 ) {
                currenRomanNumber = RN[pi].name() + currenRomanNumber;
                currentRomanValue = currentRomanValue - RN[pi].getValue();
                i--;
            } else if (i < 2 && i >= 0){
                currenRomanNumber = RN[0].name();
                currentRomanValue = RN[0].getValue();
                i = i >0 ? i-- : 0 ;
            } else {
                currenRomanNumber = "";
                currentRomanValue = 0;
                i--;
            }

            ArabicNumber -= currentRomanValue;
            result += currenRomanNumber;

            System.out.println(i + " - " + currenRomanNumber + " == " + currentRomanValue + " :: " + ArabicNumber+" -- " + result);
        }
        return result;
    }

    private static String chOperator = "+\\-*/";
    private static String chRoman = "IVXLCDMZ";
    private static String chArabic = "0-9";

    private static boolean isOperator(String str){
        Pattern ptOperator = Pattern.compile("['"+chOperator+"]");
        return ptOperator.matcher(str).find();
    }
    private static boolean isRoman(String str){
        Pattern ptRoman = Pattern.compile("['"+chRoman+"]");
        return ptRoman.matcher(str).find();
    }
    private static boolean isArabic(String str){
        Pattern ptArabic = Pattern.compile("['"+chArabic+"]");
        return ptArabic.matcher(str).find();
    }
    private static boolean isIllegalChar(String str){
        Pattern ptIllegalChar = Pattern.compile("[^"+chOperator+chRoman+chArabic+" ]");
        return ptIllegalChar.matcher(str).find();
    }
    private  static boolean isCorrectOperand (String sOpereand){
        Pattern ptCheck = Pattern.compile(" ");
        return ! ptCheck.matcher(sOpereand.trim()).find();
    }

    private static void executeException(String errorCode, String errorMessage){
        // Код генерации пользовательского ИСКЛЮЧЕНИЯ
    }

    public static String fnCalculator(String Argumets){

        String result = "";
        // Блок проверок коррекности данных:
        boolean bIllegalChar = isIllegalChar(Argumets);
        boolean bOperator = isOperator(Argumets);
        boolean bArabic = isArabic(Argumets);
        boolean bRoman = isRoman(Argumets);

        // Первая реакция:
        if (bIllegalChar || ! bOperator || bArabic && bRoman) {
            errorMessage = "Некорректные данные:" +
                    (bIllegalChar ? " Неодопустимые символы;" : "") +
                    ( ! bOperator ? " Нет Оператора;" : "") +
                    (bRoman &&  bArabic ? " Римские и Арабские числа вместе;" : "") + "!!!";
// ?            errorCode = 9999;
            errorStatus = true;
            executeException(errorCode, errorMessage);
        }

        // Получаем аргументы и операции
        String[] Args = Argumets.split("((?<=["+chOperator+"]))|(?=["+chOperator+"])");

        // Вторая проверка - количество Операндов: их должно быть 2 (два)
        if (Args.length != 3) {
            errorMessage =  Args.length > 3 ? "Много данных!" : " Мало данных";
//            errorCode = ????;
            errorStatus =true;
            executeException(errorCode,errorMessage);
        } else {
            if ( ! isCorrectOperand(Args[0]) &&isCorrectOperand(Args[2])) {
                errorMessage = "Неверный операнд!";
//              errorCode = ????;
                errorStatus =true;
                executeException(errorCode, errorMessage);
            }

            // Норамлизация данных
            String sOperation = Args[1];
            int iOperandA = bRoman ? convertRomanToArabic(Args[0]): Integer.getInteger(Args[0]);
            int iOperandB = bRoman ? convertRomanToArabic(Args[2]): Integer.getInteger(Args[2]);

            int iResult = 0;

            // блок вычислений
            switch (Args[1]){
                case ("+"):
                    iResult = iOperandA + iOperandB;
                    break;
                case  ("-"):
                    iResult =  iOperandA - iOperandB;
                    if (bRoman && iResult < 0){
                        errorStatus =true;
                        errorMessage = "Отрицательное число недопустимо!";
//                        errorCode = ????;
                        executeException(errorCode,errorMessage);
                    }
                    break;
                case ("*"):
                    iResult = iOperandA * iOperandB;
                    break;
                case ("/"):
                    if(iOperandB ==0){
                        errorStatus = true;
//                        errorCode = ????;
                        errorMessage = "Деление на ноль не допустимо!";
                        executeException(errorCode,errorMessage);
                    } else {
                        iResult = iOperandA / iOperandB;
                    }
                default:
                    errorStatus = true;
                    errorMessage = "Неизвестная ошибка!";
//                    errorCode = ????;
                    executeException(errorCode,errorMessage);
            }
            result = bRoman ? convertArabiToRoman(iResult) : String.valueOf(iResult);
       }
        return result;

    }

    public static void main(String[] args) {

        System.out.println("\tТестовое задание по программе Java-разработчик\r\n"+
                "\t\t- по заданию: входные данные - пара чисел и простая арифметическая операция;\r\n"+
                "\t\t- числа должны быть либо Римские, либо Арабские;\r\n"+
                "\r\n" +
                "\tДополнительно введен функционал тестирования:\r\n" +
                "\t\t- Auto - эммитация ввода разных комбинаций данных и \r\n" +
                "\t\t\t\t тестирование КАЛЬКУЛЯТОРА;\r\n" +
                "\t\t- AAAA - проверка конвертации Arabic - Roman - Arabic\r\n" +
                "\t\t- RRRR - проверка конвертации Roman - Arabic - Roman \r\n" +
                "\t\t- **** - ввод данных для теста с клавиатуры без перегрузки программмы.");

        Scanner in = new Scanner(System.in);
        System.out.println("");

//        String Args = in.nextLine().toUpperCase();


        System.out.println("**********************************\r\n"+
                "\t Тестовые данные\r\n"+
                                   "-------------------------------------------------------");
        String[] strings = {"1 / 3","X - VI", "V + 8", "VIII * II", "V - XI", "6 / 0", "1 ! 2", "X $ L", "!wQEDsXLI-1", "2+3-5*6/7"};

        System.out.println("String\t\t\t\tIllegal?\tOperator?\tRoman?\tArabic?");
        for (String str : strings) {
            System.out.println(str+"\t\t "+isIllegalChar(str)+
                    "\t"+isOperator(str)+
                    "\t"+isRoman(str)+
                    "\t"+isArabic(str));
            String[] Args = str.split("((?<=["+chOperator+"]))|(?=["+chOperator+"])");
            System.out.println(Arrays.toString(Args)+" :: "+Args.length);
        }
    }

}
