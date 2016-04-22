package edu.purdue.krustagi;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.util.Log;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * This fragment is the "page" where the application display the log from the
 * server and wait for a match.
 *
 *  @author krustagi, lab 806
 * @author eparames, lab LN1
 * 
 */
@SuppressLint("SimpleDateFormat")
public class MatchFragment extends Fragment implements OnClickListener {

	private static final String DEBUG_TAG = "DEBUG";

	/**
	 * Activity which have to receive callbacks.
	 */
	private StartOverCallbackListener activity;

	/**
	 * AsyncTask sending the request to the server.
	 */
	private Client client;

	/**
	 * Coordinate of the server.
	 */
	private String host;
	private int port;
	private TextView log, T2, P2, F2, congrats;

	/**
	 * Command the user should send.
	 */
	private String command;

	// TODO: your own class fields here

	// Class methods
	/**
	 * Creates a MatchFragment
	 * 
	 * @param activity
	 *            activity to notify once the user click on the start over
	 *            Button.
	 * @param host
	 *            address or IP address of the server.
	 * @param port
	 *            port number.
	 * 
	 * @param command
	 *            command you have to send to the server.
	 * 
	 * @return the fragment initialized.
	 */
	// TODO: you can add more parameters, follow the way we did it.
	// ** DO NOT CREATE A CONSTRUCTOR FOR MatchFragment **
	public static MatchFragment newInstance(StartOverCallbackListener activity,
			String host, int port, String command) {
		MatchFragment f = new MatchFragment();

		f.activity = activity;
		f.host = host;
		f.port = port;
		f.command = command;

		return f;
	}

	/**
	 * Called when the fragment will be displayed.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		View view = inflater.inflate(R.layout.match_fragment_layout, container,
				false);

		/**
		 * Register this fragment to be the OnClickListener for the startover
		 * button.
		 */
		view.findViewById(R.id.bu_start_over).setOnClickListener(this);

		// TODO: import your Views from the layout here. See example in
		// ServerFragment.
		log = (TextView) view.findViewById(R.id.log);
		T2 = (TextView) view.findViewById(R.id.T2);
		P2 = (TextView) view.findViewById(R.id.P2);
		F2 = (TextView) view.findViewById(R.id.F2);
		congrats = (TextView) view.findViewById(R.id.Congrats);
		/**
		 * Launch the AsyncTask
		 */
		this.client = new Client();
		this.client.execute("");

		return view;
	}

	/**
	 * Callback function for the OnClickListener interface.
	 */
	@Override
	public void onClick(View v) {
		/**
		 * Close the AsyncTask if still running.
		 */
		this.client.close();

		/**
		 * Notify the Activity.
		 */
		this.activity.onStartOver();
	}
	class Client extends AsyncTask<String, String, String> implements Closeable {

		/**
		 * NOTE: you can access MatchFragment field from this class:
		 * 
		 * Example: The statement in doInBackground will print the message in
		 * the Eclipse LogCat view.
		 */
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;
		private String response = "";
		private boolean connected = false;
		private String name = "" ,from = "" ,to = "";
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    //String formattedDate = df.format(c.getTime());
		/**
		 * The system calls this to perform work in a worker thread and delivers
		 * it the parameters given to AsyncTask.execute()
		 */
		protected String doInBackground(String... params) {

			/**
			 * TODO: Your Client code here.
			 */
			InetAddress serverAddr;
			try {
				System.out.println("VALUE OF HOST:" + host + connected);
				serverAddr = InetAddress.getByName(host);
				System.out.println("VALUE OF HOST:" + host + connected);
				socket = new Socket(serverAddr, port);
				connected = true;
				System.out.println("INSIDE doInBackground PLS HELP   " + connected);
				out = new PrintWriter(socket.getOutputStream(), true);
	            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            out.println(command);
	            response = in.readLine();
	            publishProgress();
			} catch (Exception e) {
				System.out.println("inside unknownhost");
				publishProgress();
				System.out.println("going out of unknownhost");
			} 
			Log.d(DEBUG_TAG, String
					.format("The Server at the address %s uses the port %d",
							host, port));

			Log.d(DEBUG_TAG, String.format(
					"The Client will send the command: %s", command));

			return "";
		}
		public void close() {
                    // TODO: Clean up the client
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e) { }
		}

		/**
		 * The system calls this to perform work in the UI thread and delivers
		 * the result from doInBackground()
		 */

		// TODO: use the following method to update the UI.
		// ** DO NOT TRY TO CALL UI METHODS FROM doInBackground!!!!!!!!!! **

		@SuppressLint("SimpleDateFormat")
		/**
		 * Method executed just before the task.
		 */
		@Override
		protected void onPreExecute() {
			int i = 0;
			Scanner sc = new Scanner(command);
	        sc.useDelimiter(",");
	        while (sc.hasNext()) {
	            String st = sc.next();
	            if (i == 0) { name = st; }
	            else if (i == 1) { from = st; }
	            else if (i == 2) { to = st; }
	            i++;
	        }
	        sc.close();
	        Calendar c = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			log.setText("[" + df.format(c.getTime()) + "] Connected to Server ! \n\n");
	        log.append("[" + df.format(c.getTime()) + "] " + name + ", requested to move from " + from + " to " + to + "\n\n");
	        log.append("[" + df.format(c.getTime()) + "] Waiting for Response... \n\n");
//			if (connected == false) {
//				System.out.println("Inside if connected");
//	        	log.setText("[" + df.format(c.getTime()) + "] Connecting to Server... Error ! Please Start Over.\n\n");
//	        	System.out.println("Settext Done");
//	        }
		}

		/**
		 * Method executed once the task is completed.
		 */
		@Override
		protected void onPostExecute(String result) {
		}

		/**
		 * Method executed when progressUpdate is called in the doInBackground
		 * function.
		 */
		@SuppressLint("SimpleDateFormat")
		@Override
		protected void onProgressUpdate(String... result) {
			System.out.println("INSIDE ONPROGRESSUPDATE PLS HELP");
			if (connected == false) {
				log.setText("");
				log.clearComposingText();
				System.out.println("Inside if connected");
				Calendar c = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        	log.setText("[" + df.format(c.getTime()) + "] Connecting to Server... Error ! Please Start Over.\n\n");
	        	System.out.println("Settext Done");
	        }
	        else {
	        	Calendar c = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        log.setText("[" + df.format(c.getTime()) + "] Connected to Server ! \n\n");
		        log.append("[" + df.format(c.getTime()) + "] " + name + ", requested to move from " + from + " to " + to + "\n\n");
		        log.append("[" + df.format(c.getTime()) + "] Waiting for Response... \n\n");
		        if (response.startsWith("RESPONSE: ")) {
		        	int i = 0;
					Scanner sc = new Scanner(response.substring(9));
					System.out.println(response.substring(9));
			        sc.useDelimiter(",");
			        while (sc.hasNext()) {
			            String st = sc.next();
			            if (i == 0) { name = st; }
			            else if (i == 1) { from = st; }
			            else if (i == 2) { to = st; }
			            i++;
			        }
			        sc.close();
		        	log.append("[" + df.format(c.getTime()) + "] Match Found ! Look for the details below. \n\n");
		        	P2.append(name + "       ");
		        	T2.append(to + "       ");
		        	F2.append(from + "        ");
		        	congrats.setVisibility(0);
                	out.println(":ACK");
                }
		        
		        else {
		        	log.append("[" + df.format(c.getTime()) + "] Error: Connection Reset.");
		        }
			}
		}
	}

}
