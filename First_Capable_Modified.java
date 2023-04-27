import java.io.*;
import java.net.*;

public class First_Capable_Modified{
	Socket s;
	BufferedReader din;
	DataOutputStream dout;
    String msg;
    int nRecs;
	int recSize;
	String [] lines;
    String[] Servers;
	int lrgCore = 0;
    String lrgServerType;
    String fcServerType;
    String SchdMsg;
    int lrgCount = 0;
	int x = 0;
	int jobIndex = 0;
    int core;
    int memory;
    int disk;
	
	public First_Capable_Modified(String add, int port) throws Exception{
		s = new Socket(add, port);
		din = new BufferedReader(new InputStreamReader(s.getInputStream()));
	    dout = new DataOutputStream(s.getOutputStream());	
	}
	public void sendMsg(String m) throws Exception{
		this.dout.write((m+"\n").getBytes("UTF-8"));
        dout.flush();
	}

	public String rcvMsg() throws Exception{
		msg = this.din.readLine();
		return msg;
	}
	
	public static void main(String[] args) throws Exception{
		
		First_Capable_Modified K = new First_Capable_Modified("127.0.0.1",50000);
		K.sendMsg("HELO");
		System.out.println(K.rcvMsg());
		K.sendMsg("AUTH" + " " +System.getProperty("user.name"));
		System.out.println(K.rcvMsg());
		K.sendMsg("REDY");
        K.rcvMsg();
        K.extractJob();
        K.fcSchedule();

        K.sendMsg("QUIT");
        K.rcvMsg();
        K.din.close();
        K.s.close();
        
	}

    public void terminate() throws Exception{
        sendMsg("QUIT");
        rcvMsg();
        if(msg.equals("QUIT")){
            this.s.close();
        }
    }
	
	public int extractJob() throws Exception{
		String [] jobInfo;
		jobInfo = msg.split("\\s+");
       
        while(jobInfo[0].equals("JCPL")){
			sendMsg("REDY");
            rcvMsg();
            jobInfo = msg.split("\\s+");
        }
        if(jobInfo[0].equals("JOBN")){
            jobIndex = Integer.parseInt(jobInfo[2]);
            core = Integer.parseInt(jobInfo[4]);
            memory = Integer.parseInt(jobInfo[5]);
            disk = Integer.parseInt(jobInfo[6]);
        }
        
        else if(jobInfo[0].equals("NONE")){
            jobIndex = -1;
        }
		return jobIndex;
	}

	public void schedule() throws Exception{
        sendMsg("SCHD "+ Integer.toString(jobIndex) + " " + lrgServerType + " " + Integer.toString(jobIndex%lrgCount));
        rcvMsg();
		while(jobIndex >= 0){
            sendMsg("REDY");
            rcvMsg();
            extractJob();
            if(jobIndex >=0){
                sendMsg("SCHD "+ Integer.toString(jobIndex) + " " + lrgServerType + " " + Integer.toString(jobIndex%lrgCount));
                rcvMsg();
            }
            else{
                break;
            }
        }
		
	}

    public void fcSchedule() throws Exception{
        while(jobIndex>=0){
            sendMsg("GETS Capable "+ Integer.toString(core) + " " + Integer.toString(memory) + " " + Integer.toString(disk));
            rcvMsg();
            fcSplitServers();
            sendMsg("SCHD " + Integer.toString(jobIndex) + " " + fcServerType);
            rcvMsg();
            sendMsg("REDY");
            rcvMsg();
            extractJob();
        }
    }

	public int splitServers() throws NumberFormatException, IOException, Exception{	//start splitServers after the client sends GETS ALL
		while (!msg.equals(".")){
			String [] line = msg.split("\\s+");
			if(line[0].equals("DATA")){
				nRecs = Integer.parseInt(line[1]);		//finds out Number of records
				recSize = Integer.parseInt(line[2]);	
				sendMsg("OK");
				for(int i = 0; i < nRecs; i++){
                    msg = rcvMsg();
					String cores[] = msg.split("\\s+");
                    int currentCore = Integer.parseInt(cores[4]);
					if (currentCore > lrgCore){
                        lrgServerType = cores[0];
						lrgCore = currentCore;	//largest core updates to new largest
						lrgCount = 1;		 
                    }
					else if(currentCore == lrgCore){	//if this server core is the same as the previous 
                        if(lrgServerType.equals(cores[0])){
                            lrgCount++;
                        }  	   
				    }
                }
				sendMsg("OK");
				rcvMsg();	//receives "."
			}
		}
        return lrgCore;
    }
    public String fcSplitServers() throws NumberFormatException, IOException, Exception{	//start splitServers after the client sends GETS ALL
		while (!msg.equals(".")){
			String [] line = msg.split("\\s+");
			if(line[0].equals("DATA")){ 
				nRecs = Integer.parseInt(line[1]);		//finds out Number of records
				recSize = Integer.parseInt(line[2]);	
				sendMsg("OK");
                int count = 0;
				for(int i = 0; i < nRecs; i++){
                    msg = rcvMsg();
					String cores[] = msg.split("\\s+");
                    if(count == 0){
                        fcServerType = cores[0] + " " +cores[1];
                        count ++;
                    }
                    else{
                        continue;
                    }
                    // int currentCore = Integer.parseInt(cores[4]);
					// if (currentCore > lrgCore){
                    //     lrgServerType = cores[0];
					// 	lrgCore = currentCore;	//largest core updates to new largest
					// 	lrgCount = 1;		 
                    // }
					// else if(currentCore == lrgCore){	//if this server core is the same as the previous 
                    //     if(lrgServerType.equals(cores[0])){
                    //         lrgCount++;
                    //     }  	   
				    // }
                }
				sendMsg("OK");
				rcvMsg();	//receives "."
			}
		}
        return fcServerType;
    }

    
}