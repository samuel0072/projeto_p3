public class iFace {

    int[][] network                      = new int[100][100];
    int[] size_messages_received         = new int[100];
    int[] size_messages_sent             = new int[100];
    String[] network_name                = new String[100];
    String[] network_login               = new String[100];
    String[] network_password            = new String[100];
    String[][] network_messages_received = new String[100][100];
    String[][] network_messages_sent     = new String[100][100];
    String[] birthday                    = new String[100];
    String[] mother                      = new String[100];
    String[] father                      = new String[100];

    int actual_size = 0;

    /*Variables about the bucket*/

    int size_bucket = 0;
    String[] bucket_name                = new String[50];
    String[] bucket_login               = new String[50];
    String[] bucket_password            = new String[50];
    int[][] bucket_friends              = new int[50][50];
    String[][] bucket_masseges_sent     = new String[50][50];
    String[][] bucket_masseges_received = new String[50][50];

    /*Variables about the communities*/
    int[][] community  = new int[100][100];
    String[] community_name = new String[100];
    int[] community_owner = new int[100];
    String[] community_description = new String[100];



    public static void main(String Args[]) {

    }

    public void add_friends(int target, int sender) {
        network[target][sender] = 2;
    }

    public void create_account( String name, String password, String login) {
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

    public void send_messages(String message, int i_target, int i_sender) {

        String complete_massege1 = "You have a new message from: "+network_name[i_sender]+":\n"+message;
        String complete_massege2 = "You have sent a new message to:"+network_name[i_target]+":\n"+message;

        network_messages_received[i_target][(size_messages_received[i_target]++)%100] = complete_massege1;
        network_messages_sent[i_sender][(size_messages_sent[i_sender]++)%100]         = complete_massege2;
    }

    public int search_by_name(String target) {
        for(int i = 0; i < 100; i++) {
            if( (network_name[i]!= null) && (target.equals(network_name[i])) ) {
                return i;
            }
        }
        return -1;//User not found
    }

    public void edit(int target, String new_name, String new_password, String new_login, String new_birthday,
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

    public void delete_user(int target) {
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

    public void go_to_bucket(int target) {
        bucket_name[ size_bucket%50 ] = network_name[ target ];
        bucket_login[ size_bucket%50 ] = network_login[ target ];
        bucket_password[ size_bucket%50 ] = network_password[ target ];
        bucket_friends[ size_bucket%50 ] = network[ target ];
        bucket_masseges_sent[ size_bucket%50 ] = network_messages_sent[ target ];
        bucket_masseges_received[ size_bucket%50 ] = network_messages_received[ target ];
        size_bucket++;
    }

    public void recovery_account(String login, String password) {
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

    public void create_community(String name, String description, int owner) {
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

    public void add_member(int owner, int member, int commu)
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

    public int search_community__by_name(String target) {
        for(int i = 0; i < 100; i++) {
            if( (community_name[i]!= null) && (target.equals(network_name[i])) ) {
                return i;
            }
        }
        return -1;//Community not found
    }
}
