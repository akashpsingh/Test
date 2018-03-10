package in.wynk.expenseapplication.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.wynk.expenseapplication.ExpenseStore;
import in.wynk.expenseapplication.R;
import in.wynk.expenseapplication.User;
import in.wynk.expenseapplication.Utils;

/**
 * Created by Akash on 10/03/18.
 */

public class AddTransactionActivity extends AppCompatActivity {

    private static final String Tag = "AddTransactionActivity";

    private static final int PICK_CONTACT = 100;

    private EditText etDetail, etAmount;

    private Button btnAddParticipant, btnAdd;

    private LinearLayout llTransactions;

    private List<UserViewHolder> viewHolders = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        findViews();
        bindViews();
        addCurrentUserToList();
    }

    private void addCurrentUserToList() {
        addUserToList(ExpenseStore.getInstance().getMe().getName());
    }

    private void bindViews() {
        btnAddParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickContacts();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputDataValid()) {
                    extractDataFromViews();
                    finish();
                }
            }
        });
    }

    private boolean isInputDataValid() {
        String totalAmount = etAmount.getText().toString();
        if (!Utils.isNumber(totalAmount)) {
            setError(etAmount);
            return false;
        }

        if (viewHolders.size() < 2) {
            Utils.showToast(this, "Add More Participants");
            return false;
        }

        // check all paid and share is number
        for (UserViewHolder holder : viewHolders) {

            if (!Utils.isNumber(holder.etPaid.getText().toString())) {
                setError(holder.etPaid);
                return false;
            }

            if (!Utils.isNumber(holder.etShare.getText().toString())) {
                setError(holder.etShare);
                return false;
            }
        }

        // check total paid and total share is not greater than amount
        double totalPaid = 0, totalShare = 0;
        for (UserViewHolder holder : viewHolders) {
            totalPaid += Long.parseLong(holder.etPaid.getText().toString());
            totalShare += Long.parseLong(holder.etShare.getText().toString());
        }

        if (Long.parseLong(totalAmount) < totalPaid) {
            Utils.showToast(this, "Total amount paid is greater than transaction amount.");
            return false;
        }

        if (Long.parseLong(totalAmount) < totalShare) {
            Utils.showToast(this, "Total share is greater than transaction amount.");
            return false;
        }

        return true;
    }

    private void setError(EditText et) {
        et.setError("Invalid");
    }

    private void extractDataFromViews() {
        for (UserViewHolder holder: viewHolders) {
            long paid = Long.parseLong(holder.etPaid.getText().toString());
            long share = Long.parseLong(holder.etShare.getText().toString());
            String name = holder.tvName.getText().toString();
            User user = new User(name, paid - share);
            ExpenseStore.getInstance().updateUserBalance(user);
        }
    }

    private void findViews() {
        etDetail = findViewById(R.id.et_transaction_detail);
        etAmount = findViewById(R.id.et_transaction_amount);
        btnAddParticipant = findViewById(R.id.btn_add_participant);
        btnAdd = findViewById(R.id.btn_add_transaction);
        llTransactions = findViewById(R.id.ll_transactions);
    }

    private void pickContacts() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, PICK_CONTACT);
    }

    private void addUserToList(String name) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_single_person, llTransactions, false);
        UserViewHolder userViewHolder = new UserViewHolder(view);
        viewHolders.add(userViewHolder);
        userViewHolder.bindView(name);
        llTransactions.addView(userViewHolder.getView());
    }

    private void removeUserView(UserViewHolder holder) {
        int index = viewHolders.indexOf(holder);
        viewHolders.remove(holder);
        if (index != -1) {
            llTransactions.removeViewAt(index);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_CONTACT:
                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    Uri contactData = data.getData();
                    String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
                    Cursor cursor = getContentResolver().query(contactData, projection, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        Log.v(Tag, "name= " + name);
                        cursor.close();
                        addUserToList(name);
                    }
                }
                break;
        }
    }

    class UserViewHolder {

        TextView tvName;

        EditText etPaid, etShare;

        Button btnDelete;

        View view;

        public UserViewHolder(View view) {
            this.view = view;
            tvName = view.findViewById(R.id.tv_name);
            etPaid = view.findViewById(R.id.et_paid);
            etShare = view.findViewById(R.id.et_share);
            btnDelete = view.findViewById(R.id.btn_delete);
        }

        public void bindView(String name) {
            tvName.setText(name);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeUserView(UserViewHolder.this);
                }
            });
        }

        public View getView() {
            return view;
        }
    }

}
