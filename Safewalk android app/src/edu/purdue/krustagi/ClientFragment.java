package edu.purdue.krustagi;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * This fragment is the "page" where the user inputs information about the
 * request, he/she wishes to send.
 *
 * @author Kush Rustagi (krustagi@purdue.edu) Lab Section: 806
 * @author Eehita Parameswaran (eparames@purdue.edu) Lab Section: LN1
 */
public class ClientFragment extends Fragment implements OnClickListener {
	
	 private Spinner spinner1, spinner2;
	 private RadioGroup radioGroup;
	 private RadioButton radioOneButton, radioTwoButton;
	 private EditText edittext;

	/**
	 * Activity which have to receive callbacks.
	 */
	private SubmitCallbackListener activity;

	/**
	 * Creates a ProfileFragment
	 * 
	 * @param activity
	 *            activity to notify once the user click on the submit Button.
	 * 
	 *            ** DO NOT CREATE A CONSTRUCTOR FOR MatchFragment **
	 * 
	 * @return the fragment initialized.
	 */
	// ** DO NOT CREATE A CONSTRUCTOR FOR ProfileFragment **
	public static ClientFragment newInstance(SubmitCallbackListener activity) {
		ClientFragment f = new ClientFragment();

		f.activity = activity;
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

		View view = inflater.inflate(R.layout.client_fragment_layout,
				container, false);

		/**
		 * Register this fragment to be the OnClickListener for the submit
		 * Button.
		 */
		view.findViewById(R.id.bu_submit).setOnClickListener(this);

		// TODO: import your Views from the layout here. See example in
		// ServerFragment.
		radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup1);
		spinner1 = (Spinner) view.findViewById(R.id.spinner1);
		spinner2 = (Spinner) view.findViewById(R.id.spinner2);
		radioOneButton = (RadioButton) view.findViewById(R.id.radio0);
		radioTwoButton = (RadioButton) view.findViewById(R.id.radio1);
		this.edittext = (EditText) view.findViewById(R.id.editText1);

		return view;
	}

	/**
	 * Callback function for the OnClickListener interface.
	 */
	@Override
	public void onClick(View v) {
		this.activity.onSubmit();
	}
	
	public String getName() {
		if (edittext.getText().toString().equals(""))
			return "0";
		else return edittext.getText().toString();
	}
	public int getType() {
		if(radioGroup.getCheckedRadioButtonId() == radioOneButton.getId())
			return 1;
		else if (radioGroup.getCheckedRadioButtonId() == radioTwoButton.getId())
			return 2;
		else return 0;
	}
	public String getTo() {
		if (spinner2.getSelectedItemPosition() == 0)
			return "CL50";
		else if (spinner2.getSelectedItemPosition() == 1)
			return "PMU";
		else if (spinner2.getSelectedItemPosition() == 2)
			return "EE";
		else if (spinner2.getSelectedItemPosition() == 3)
			return "LWSN";
		else if (spinner1.getSelectedItemPosition() == 4)
			return "PUSH";
		else return "*";
	}
	public String getFrom() {
		if (spinner1.getSelectedItemPosition() == 0)
			return "CL50";
		else if (spinner1.getSelectedItemPosition() == 1)
			return "PMU";
		else if (spinner1.getSelectedItemPosition() == 2)
			return "EE";
		else if (spinner1.getSelectedItemPosition() == 3)
			return "LWSN";
		else return "PUSH";
	}
}

