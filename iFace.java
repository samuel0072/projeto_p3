import java.util.Scanner;

public class iFace {

    static Scanner reader = new Scanner ( System.in );

    static int[][] network                      = new int[100][100];
    static int[] size_messages_received         = new int[100];
    static int[] size_messages_sent             = new int[100];
    static String[] network_name                = new String[100];
    static String[] network_login               = new String[100];
    static String[] network_password            = new String[100];
    static String[][] network_messages_received = new String[100][100];
    static String[][] network_messages_sent     = new String[100][100];
    static String[] birthday                    = new String[100];
    static String[] mother                      = new String[100];
    static String[] father                      = new String[100];

    static int actual_size = 0;

    /*Variables about the bucket*/

    static int size_bucket = 0;
    static String[] bucket_name                = new String[50];
    static String[] bucket_login               = new String[50];
    static String[] bucket_password            = new String[50];
    static int[][] bucket_friends              = new int[50][50];
    static String[][] bucket_masseges_sent     = new String[50][50];
    static String[][] bucket_masseges_received = new String[50][50];

    /*Variables about the communities*/
    static int[][] community              = new int[100][100];
    static String[] community_name        = new String[100];
    static int[] community_owner          = new int[100];
    static String[] community_description = new String[100];
    static String[][] community_messages  = new String[100][100];
    static int[] size_messages_community  = new int[100];


    public static void main(String Args[]) {
        test ();
        //String a = reader.nextLine ();
        //System.out.println ( a );
    }

    public static void add_friends(int target, int sender) {
        network[target][sender] = 2;
    }

    public static void create_account( String name, String password, String login) {
        if(actual_size < 100) {
            int i;
            for(i = 0; i < 100; i++) {
                if(network_name[i] == null) {
                    break;
                }
            }

            network_name[i] = name;
            network_login[i] = login;
            network_password[i] = password;

            actual_size++;
        }
        else {
            System.out.println("Network overflow!");
        }
    }

    public static void send_messages(String message, int i_target, int i_sender) {

        String complete_massege1 = "You have a new message:\n"+message+"\nmessage sent by: "+network_name[i_sender];
        String complete_massege2 = "You have sent a new message to:"+network_name[i_target]+":\n"+message;

        network_messages_received[i_target][(size_messages_received[i_target]++)%100] = complete_massege1;
        network_messages_sent[i_sender][(size_messages_sent[i_sender]++)%100]         = complete_massege2;
    }

    public static int search_by_name(String target) {
        for(int i = 0; i < 100; i++) {
            if( (network_name[i]!= null) && (target.equals(network_name[i])) ) {
                return i;
            }
        }
        return -1;//User not found
    }

    public static void edit(int target, String new_name, String new_password, String new_login, String new_birthday,
                            String new_mother, String new_father) {
        if(!"".equals(new_name))
        {
            network_name = clear_string(target, network_name);
            network_name[target] = new_name;
        }
        if(!"".equals(new_password))
        {
            network_password = clear_string(target, network_password);
            network_password[target] = new_password;
        }
        if(!"".equals(new_login))
        {
            network_login = clear_string(target, network_login);
            network_login[target] = new_login;
        }
        if(!"".equals(new_birthday))
        {
            birthday = clear_string(target, birthday);
            birthday[target] = new_birthday;
        }
        if(!"".equals(new_mother))
        {
            mother = clear_string(target, mother);
            mother[target] = new_mother;
        }
        if(!"".equals(new_father))
        {
            father = clear_string(target, father);
            father[target] = new_father;
        }
    }

    public static String[] clear_string(int target, String[] actual) {
        String[] new_string_vector = new String[100];
        int i;
        for(i = 0; i < 100; i++)
        {
            if(i!= target)
            {
                new_string_vector[i] = actual[i];
            }
        }
        return new_string_vector;
    }

    public static void delete_user(int target) {
        go_to_bucket ( target );
        birthday = clear_string(target, birthday);
        mother =  clear_string(target, mother);
        father = clear_string(target, father);
        network_password = clear_string(target, network_password);
        network_login = clear_string(target, network_login);
        network_name = clear_string(target, network_name);
        network_messages_sent[target] = clear_string ( target, network_messages_sent[target] );
        network_messages_received[target] = clear_string ( target, network_messages_received[target] );

        size_messages_sent[target] = 0;
        size_messages_received[target] = 0;

        int i;

        for(i = 0; i < 100; i++)
        {
            network[target][i]  = 0;
            network[i][target]  = 0;
            community[i][target] = 0;
        }
    }

    public static void go_to_bucket(int target) {
        bucket_name[ size_bucket%50 ] = network_name[ target ];
        bucket_login[ size_bucket%50 ] = network_login[ target ];
        bucket_password[ size_bucket%50 ] = network_password[ target ];
        bucket_friends[ size_bucket%50 ] = network[ target ];
        bucket_masseges_sent[ size_bucket%50 ] = network_messages_sent[ target ];
        bucket_masseges_received[ size_bucket%50 ] = network_messages_received[ target ];
        size_bucket++;
    }

    public static void recovery_account(String login, String password) {
        int i;

        for(i = 0; i < 50; i++) {
            if(bucket_login[i]!= null) {
                if(bucket_login[i].equals ( login ) && bucket_password[i].equals ( password )) {
                    break;
                }
            }
        }

        if(i < 50) {

            create_account ( bucket_name[i], bucket_password[i], bucket_login[i] );

            int target = search_by_name ( bucket_name[i] ), j;

            for(j = 0; j < 100; j++)
            {
                if(bucket_friends[i][j] == 1)
                {
                    add_friends ( j, target );
                }
            }
            network_messages_sent[target]     = bucket_masseges_sent[target];
            network_messages_received[target] = bucket_masseges_received[target];
        }
        else
        {
            System.out.println ( "User not found!" );
        }
    }

    public static void create_community(String name, String description, int owner) {
        int i;
        for (i = 0; i < 100; i++) {
            if(community_name[i] == null)
            {
                break;
            }
        }
        if(i < 100) {
            community[i][owner] = 1;
            community_name[i] = name;
            community_description[i] = description;
            community_owner[i] = owner;
        }
        else {
            System.out.println("Cannot create a new community!");
        }
    }

    public static void add_member(int owner, int member, int commu)
    {
        if(community_owner[commu] == owner)
        {
            community[commu][member] = 1;
        }
        else
        {
            System.out.println("You're not allowed to do this!");
        }
    }

    public static int search_community__by_name(String target) {
        for(int i = 0; i < 100; i++) {
            if( (community_name[i]!= null) && (target.equals(community_name[i])) ) {
                return i;
            }
        }
        return -1;//Community not found
    }

    public static void massege_to_community(int sender_index, int community_index, String message)
    {
        String complete_message = "The user: "+network_name[sender_index]+" sent:\n"+message;
        community_messages[community_index][size_messages_community[community_index]%100] = complete_message;
        size_messages_community[community_index]++;
    }

    public static void test()
    {
        /*Descomente os teste que deseja fazer usando essa funcao*/

        //Inserindo dados
        create_account( "Samuel", "samuel", "samuel0072");
        create_account( "Allef", "alef", "alef123");
        create_account( "Alvaro", "alvaro", "alvaro123");
        create_account( "Ana", "ana", "ana123");
        create_account( "Bruno","bruno", "bruno123");
        create_account( "Carlos", "carlos", "carlosW1998");
        create_account( "Eric", "eric", "eric3119");
        create_account( "Everton", "everton", "eveton123");
        create_account( "Gabriel", "gabriel", "gabriel123");
        create_account( "Hugo", "hugo", "hugo123");
        create_account( "Jadson", "jadson", "crislanzinho");
        create_account( "John", "john", "john123");
        create_account( "Jonas", "jonas", "jonas123");
        create_account( "Augusto", "augusto", "augusto123");
        create_account( "Lucas", "lucas", "lucas123");
        create_account( "Marcelo C", "marcelo", "marceloc123");
        create_account( "Marcelo O", "marcelo", "marceloo123");
        create_account( "Matheus", "matheus", "matheus123");
        create_account( "Nicholas", "nicholas", "nicholas123");
        create_account( "Paulo", "paulo", "paulo123");
        create_account( "Pedro M", "pedro", "pedrom123");
        create_account( "Pedro V", "pedro", "pedrov123");
        create_account( "Phyllipe", "phyllipe", "phyllipe123");
        create_account( "Rafael", "rafael", "rafael123");
        create_account( "Raissa", "raissa", "raissa123");
        create_account( "Raul", "raul", "raul123");
        create_account( "Thiago C", "thiago", "thiagoc123");
        create_account( "Thiago J", "thiago", "thiagoj123");
        create_account( "Thiago P", "thiago", "thiagop123");
        create_account( "Thiago T", "thiago", "thiagot123");
        create_account( "Valerio", "valerio", "valerio123");
        create_account( "Victor H", "victor", "victorh123");
        create_account( "Victor M", "victor", "victorm123");
        create_account( "Baldoino", "ehsoprintf", "baldu");
        create_account( "aurelio", "123", "au");

        int index_b = search_by_name ("Baldoino"), index_s = search_by_name ( "Samuel" );
        create_community ( "iFarra", "uma festa realizada para os alunos de p3", index_b );
        create_community ( "aprenderOO", "Bora aprender OO", index_s);

        //Testes
        //add_friends_test ();
        //create_account_test ();
        //send_messages_test();
        //create_community_test ();
        //send_messages_community_test();
        //add_member_test();
        //edit_test();
        remove_test();
        //recovery_test();

    }

    public static void add_friends_test()
    {
        int i,j, s ,d;
        System.out.println("Banco de dados do iFace:\n");

        for(i = 0; i < 100; i++) {
            if ( network_name[i]!= null ) {
                System.out.println("\t"+(i+1)+" : "+ network_name[i]);
            }
        }
        System.out.println("Digite crtl+ f4 finalizar a qualquer momento!");

        do
        {
            String sender, target;

            System.out.println("Digite o nome de quem deseja enviar o convite de amizade:");
            sender = reader.next (  );

            System.out.println("Digite o nome de quem vai receber o pedido de amizade!");
            target = reader.next();

            s = search_by_name ( sender );
            d = search_by_name ( target );

            if((s != -1) && (d != -1)){
                add_friends ( d, s );
            }
            else{
                System.out.println("Algum nome foi digitado errado!");
            }
            System.out.println("Continuar? 1 - sim \t2 - nao");
            d = reader.nextInt ();
        }while(d!=2);
        for(i = 0; i < 100; i++) {
            if(network_name[i]!= null)
            {
                System.out.print(network_name[i]+" Tem solicitacoes pendentes de: ");
                for(j = 0; j < 100; j++)
                {
                    if(network[i][j] == 2)
                    {
                        System.out.print(network_name[j]+" ");
                    }
                }
                System.out.println();
            }
        }
    }

    public static void create_account_test()
    {
        int d, i;
        System.out.println("Digite crtl+ f4 finalizar a qualquer momento!");

        do {
            String name, login, password;
            System.out.println("Digite o nome:");
            name = reader.next (  );
            System.out.println("Digite o login:");
            login = reader.next ();
            System.out.println("Digite a senha");
            password = reader.next();
            create_account( name, password, login );
            System.out.println("Continuar? 1 - sim \t2 - nao");
            d = reader.nextInt ();
        }while(d != 2);

        System.out.println("Banco de dados do iFace:\n");

        for(i = 0; i < 100; i++) {
            if ( network_name[i]!= null ) {
                System.out.println("\t"+(i+1)+" : "+ network_name[i]);
            }
        }
    }

    public static void send_messages_test()
    {
        int d, i, j;
        System.out.println("Digite crtl+ f4 finalizar a qualquer momento!");

        do {
            String name1, name2, message;
            System.out.println("Digite o nome de quem vai enviar a mensagem:");
            name1 = reader.nextLine();
            System.out.println("Digite o nome de quem vai receber a mensagem:");
            name2 = reader.nextLine();

            int s, r;
            s = search_by_name ( name1 );
            r = search_by_name ( name2 );

            if(s!= -1 && r != -1)
            {
                System.out.println("Digite a mensagem:");
                message = reader.nextLine();
                send_messages(message, r, s);
            }
            else
            {
                System.out.println("Voce digitou algum nome errado!");
            }

            System.out.println("Continuar? 1 - sim \t2 - nao");
            d = reader.nextInt ();
        }while(d != 2);

        for(i = 0; i < 100; i++)
        {
            if(network_name[i]!= null)
            {
                System.out.print(network_name[i]+"Enviou: ");
                for(j = 0; j < 100; j++)
                {
                    if(network_messages_sent[i][j]!= null)
                    {
                        System.out.print("\'"+network_messages_sent[i][j]+"\', ");
                    }
                }
                System.out.println();
            }
        }
        for(i = 0; i < 100; i++)
        {
            if(network_name[i]!= null)
            {
                System.out.print(network_name[i]+"Recebeu: ");
                for(j = 0; j < 100; j++)
                {
                    if(network_messages_received[i][j]!= null)
                    {
                        System.out.print("\'"+network_messages_received[i][j]+"\', ");
                    }
                }
                System.out.println();
            }
        }
    }

    public static void create_community_test()
    {
        int d, i, j;
        System.out.println("Digite crtl+ f4 finalizar a qualquer momento!");

        do {
            String name, description, owner;
            int index_owner;
            System.out.println("Digite o nome da comunidade:");
            name = reader.nextLine();
            System.out.println("Digite a descricao:");
            description = reader.nextLine();
            System.out.println("Digite o nome do dono:");
            owner = reader.nextLine();

            index_owner = search_by_name( owner );
            if(index_owner!= -1)
            {
                create_community( name, description, index_owner );
            }
            else
            {
                System.out.println("voce digitou o nome do dono errado!");
            }

            System.out.println("Continuar? 1 - sim \t2 - nao");
            d = reader.nextInt ();
        }while(d != 2);
        System.out.println("Banco de dados do iFace(comunidades):");
        for(i = 0; i < 100; i++)
        {
            if(community_name[i]!= null)
            {
                System.out.println("\t"+community_name[i]+", "+community_description[i]+", "+network_name[community_owner[i]]);
            }
        }


    }

    public static void send_messages_community_test()
    {
        int d, i, j;
        System.out.println("Digite crtl+ f4 finalizar a qualquer momento!");

        do {
            String name1, community, message;
            System.out.println("Digite o nome de quem vai enviar a mensagem:");
            name1 = reader.nextLine();
            System.out.println("Digite o nome da comunidade que vai enviar a mensagem:");
            community = reader.nextLine();

            int s, r;
            s = search_by_name ( name1 );
            r = search_community__by_name ( community );

            if(s!= -1 && r != -1)
            {
                System.out.println("Digite a mensagem:");
                message = reader.nextLine();
                massege_to_community(s, r, message);
            }
            else
            {
                System.out.println("Voce digitou algum nome errado!");
            }

            System.out.println("Continuar? 1 - sim \t2 - nao");
            d = reader.nextInt ();
        }while(d != 2);

        for(i = 0; i < 100; i++)
        {
            if(community_name[i]!= null){
                System.out.print("Mensagens da comunidade "+community_name[i]+" :");
                for(j = 0; j < 100; j++)
                {
                    if(community_messages[i][j] != null){
                        System.out.print(" "+community_messages[i][j]+", ");
                    }
                }
            }
        }

    }
    public static void add_member_test()
    {
        int d, i, j;
        System.out.println("Digite crtl+ f4 finalizar a qualquer momento!");

        do {
            String name1, name2,community,  message;
            System.out.println("Digite o nome da comunidade");
            community = reader.nextLine();
            System.out.println("Digite o nome do admin:");
            name1 = reader.nextLine();
            System.out.println("Digite o nome do usuario que deseja adicionar");
            name2 = reader.nextLine();

            int s, r, c;
            s = search_by_name ( name1 );
            c = search_community__by_name ( community );
            r = search_by_name(name2);

            if(s!= -1 && r != -1 && c!= -1)
            {
                add_member(s, r, c);
            }
            else
            {
                System.out.println("Voce digitou algum nome errado!");
            }

            System.out.println("Continuar? 1 - sim \t2 - nao");
            d = reader.nextInt ();
        }while(d != 2);

        for(i = 0; i < 100; i++)
        {
            if(community_name[i]!= null){
                System.out.print("Membros da comunidade "+community_name[i]+": ");
                for(j = 0; j < 100; j++){
                    if(community[i][j] == 1){
                        System.out.print(network_name[j]+", ");
                    }
                }
            }
            System.out.println();

        }
    }

    public static void edit_test(){
        int d, i, j;
        System.out.println("Digite crtl+ f4 finalizar a qualquer momento!");

        do {
            String father_n, mother_n, birth_n, name, login, password;
            System.out.println("digite o nome do pai:");
            father_n = reader.nextLine();
            System.out.println("digite o nome da mae:");
            mother_n = reader.nextLine();
            System.out.println("digite o aniversario:");
            birth_n = reader.nextLine();
            System.out.println("digite o login:");
            login = reader.nextLine();
            System.out.println("digite o nome:");
            name = reader.nextLine();
            System.out.println("digite a senha:");
            password = reader.nextLine();

            int c = search_by_name(name);
            if(c!= -1){
                edit(c, name, password, login, birth_n, mother_n, father_n);
                print_user(c);
            }
            else{
                System.out.println("voce digitou alguma coisa errada");
            }

            System.out.println("Continuar? 1 - sim \t2 - nao");
            d = reader.nextInt ();

        }while(d != 2);
    }


    public static void  print_user(int target){
        System.out.println(network_name[target]);
        System.out.println(network_login[target]);
        System.out.println(network_password[target]);
        System.out.println(mother[target]);
        System.out.println(father[target]);
        System.out.println(birthday[target]);

        int j;
        System.out.println("amigos de :"+network_name[target]);
        for(j = 0; j < 100; j++){
                if((network[target][j] == 1) && (network_name[j]!= null)){
                    System.out.print(network_name[j]+", ");
                }
                System.out.println();
        }
    }

    public static void remove_test(){
        int d, i, j;
        System.out.println("Digite crtl+ f4 finalizar a qualquer momento!");

        do {
            String name;
            System.out.println("Digite o nome do usuario que deseja remover:");
            name = reader.nextLine();
            int c = search_by_name(name);
            if(c!= -1){
                delete_user(c);
            }
            else
            {
                System.out.println("Voce digitou o nome errado!");

            }
            System.out.println("Continuar? 1 - sim \t2 - nao");
            d = reader.nextInt();

        }while(d != 2);

        System.out.println("Banco de dados do iFace:\n");

        for(i = 0; i < 100; i++) {
            if ( network_name[i]!= null ) {
                System.out.println("\t"+(i+1)+" : "+ network_name[i]);
            }
        }
    }

    public static void recovery_test(){
        int d, i, j;
        remove_test();
        /*int a = search_by_name("Carlos");
        delete_user(a);*/
        reader.nextLine();
        System.out.println("Digite crtl+ f4 finalizar a qualquer momento!");

        System.out.println("Banco de dados do iFace:\n");

        for(i = 0; i < 100; i++) {
            if ( network_name[i]!= null ) {
                System.out.println("\t"+(i+1)+" : "+ network_name[i]);
            }
        }
        do {
            String login, password;
            System.out.println("Digite o login do user que vc quer recuperar os dados");
            login = reader.nextLine();
            System.out.println("Digite a senha do user que vc quer recuperar os dados");
            password = reader.nextLine();
            recovery_account(login, password);

            System.out.println("Continuar? 1 - sim \t2 - nao");
            d = reader.nextInt();
        }while(d != 2);

        System.out.println("Banco de dados do iFace:\n");

        for(i = 0; i < 100; i++) {
            if ( network_name[i]!= null ) {
                System.out.println("\t"+(i+1)+" : "+ network_name[i]);
            }
        }
    }
}
