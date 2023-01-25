import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Demo {

    static Connection connection = null ;
    //String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=game;user=sa;password=Prince#1";

    static String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=game;user=sa;password=Prince#1" ;

    static Statement statement = null ;

    static  Scanner sc = new Scanner( System.in ) ;



    static void printUserInfo( String name , String  username , double wallet_amount ){

        System.out.print("Name          : " + name + "\n" ) ;
        System.out.print("Username      : " + username + "\n" ) ;
        System.out.print("Wallet Amount : " + wallet_amount + "\n" ) ;

    }

    private static void  createSession() throws Exception{
        System.out.print("Enter username : ");
        String username = sc.nextLine() ;

        Statement statement = connection.createStatement() ;
        String query = "SELECT name FROM userdetails WHERE username = '" + username+ "'" ;
        ResultSet resultSetUsername = statement.executeQuery("SELECT name FROM userdetails WHERE username = '" + username+ "'" ) ;

        System.out.print( query );

        if ( resultSetUsername.next() ){
            int queryCount = 0 ;
            ResultSet resultSetAuthentication = null ;
            boolean passwordMatched = false ;



            //boolean passwordMatched = resultSetAuthentication.next() ;
            //resultSetAuthentication.next() ;
            /*
            while ( queryCount < 3 && ! passwordMatched  ){
                System.out.print("Enter Password Again Chance left " + (char)( 3 - queryCount + '0' ) );
                queryCount++ ;

                password = sc.nextLine() ;
                resultSetAuthentication = statement.executeQuery("SELECT name , wallet_amt FROM userdetails WHERE username = '" + username+ "' AND password = '"+ password + "'" ) ;
                passwordMatched = resultSetAuthentication.next() ;

            }
             */

            do{

                System.out.print("Enter Password : " );
                String password = sc.nextLine() ;

                resultSetAuthentication = statement.executeQuery("SELECT name , wallet_amt FROM userdetails WHERE username = '" + username+ "' AND password = '"+ password + "'" ) ;

                if( ! resultSetAuthentication.next() ){
                    System.out.print("Wrong password Enter password again \n");
                    passwordMatched = false ;
                }else{
                    passwordMatched = true ;
                }

                queryCount++ ;

            }while(queryCount < 3 && ! passwordMatched ) ;


            if( passwordMatched ){

                String name = resultSetAuthentication.getString(1) ;
                double wallet_amount = resultSetAuthentication.getDouble( 2 ) ;

                printUserInfo( name , username , wallet_amount );

            }else {
                System.out.print("You exceeded password limit \n");

            }

        }else{

            System.out.print("No such username exists. Create an Account ");
        }


    }

    public  static void main(String ...args ) throws Exception {

        connection = DriverManager.getConnection( connectionUrl ) ;
        //statement = connection.createStatement() ;

        createSession();


    }


}
