
public class FeedbackFragment extends Fragment {
  editText namedata, emaildata, messagedata;
  Button submit_bt;
  Firebase firebase; 

  public View onCreateView (@NonNull LayoutInflater inflater , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_feedback, container, false);

    namedata = findViewById(R.id.namedata);
    emaildata = findViewById(R.id.emaildata);
    messagedata = findViewById(R.id.messagedata);

    submit_bt = findViewById(R.id.submit_bt);
    Firebase.setAndroidContext(this);

    //firebase = new Firebase("")

    submit_bt.setOnClickListener(new View.OnClickListener()) {
      @Override
      public void onClick(View v) {
        String name = namedata.getText().toString();
        String email = emaildata.getText().toString();
        String message = messagedata.getText().toString();

        Firebase child_name = firebase.child("Name");
        child_name.setValue(name);
        if (name.isEmpty()){
          namedata.setError("This field is required.");
          submit_bt.setEnabled(false);
        } else {
          namedata.setError(null);
          submit_bt.setEnabled(true);
        }

        Firebase child_email = firebase.child("Email");
        child_email.setValue(email);
        if (email.isEmpty()) {
          emaildata.setError("This field is required.");
          submit_bt.setEnabled(false);
        } else {
          emaildata.setError(null);
          submit_bt.setEnabled(true);
        }

        Firebase child_message = firebase.child("Message");
        child_message.setValue(message);
        if (message.isEmpty()){
          messagedata.setError("This field is required.");
          submit_bt.setEnabled(false);
        } else {
          messagedata.setError(null);
          submit_bt.setEnabled(true);
        }
      }
    }
  }
}