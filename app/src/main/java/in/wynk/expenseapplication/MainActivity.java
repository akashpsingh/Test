package in.wynk.expenseapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.DragAndDropPermissions;
import android.view.DragEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import in.wynk.expenseapplication.ui.AddTransactionActivity;
import in.wynk.expenseapplication.ui.FriendsAdapter;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rv_friend_list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isReadContactsPermissionGranted(MainActivity.this)) {
                    startActivity(new Intent(MainActivity.this, AddTransactionActivity.class));
                } else {
                    Utils.showToast(MainActivity.this, "Grant read contacts permission.");
                }
            }
        });

        if (!Utils.isReadContactsPermissionGranted(this)) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_CONTACTS}, 1000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showExpenseReport();
    }

    private void showExpenseReport() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FriendsAdapter adapter = new FriendsAdapter(ExpenseStore.getInstance().getAllUsersToShow());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
