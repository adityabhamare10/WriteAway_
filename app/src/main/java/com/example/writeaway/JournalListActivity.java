package com.example.writeaway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.List;

public class JournalListActivity extends AppCompatActivity {


    //FirebaseAuth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    //Firebase Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference =
            db.collection("Journal");

    //Firebase Stroage
    private StorageReference storageReference;

    //List of Journals
    private List<Journal> journalList;

    //RecyclerView
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);
        getSupportActionBar().hide();

        // Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        // Widgets
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Post Arraylist
        journalList = new ArrayList<>();

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(JournalListActivity.this, AddJournalActivity.class);
                startActivity(i);
            }
        });





    }

    // 2 - Adding a menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int value = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_add:
                if (user != null && firebaseAuth != null) {
                    Intent i = new Intent(JournalListActivity.this,
                            AddJournalActivity.class);
                    startActivity(i);
                }
                break;

            case R.id.action_signout:
                if (user != null && firebaseAuth != null) {
                    firebaseAuth.signOut();
                    Intent i = new Intent(JournalListActivity.this, MainActivity.class);
                    startActivity(i);
                }
                break;
        }
                return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onStart() {
        super.onStart();
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                /*QueryDocumentSnapshot: is an object represents
                a single document retrieved from a firestore query*/
                //QueryDocumentSnapshot --> Document

                for(QueryDocumentSnapshot journals: queryDocumentSnapshots)
                {
                   Journal journal = journals.toObject(Journal.class);

                   journalList.add(journal);
                }

                //RecyclerView

                myAdapter = new MyAdapter(JournalListActivity.this,
                        journalList);

                recyclerView.setAdapter(myAdapter);

                myAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(JournalListActivity.this,
                    "Opps! Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}