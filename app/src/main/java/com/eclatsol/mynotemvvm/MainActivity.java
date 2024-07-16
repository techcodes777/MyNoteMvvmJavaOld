package com.eclatsol.mynotemvvm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.eclatsol.mynotemvvm.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private NoteViewModel noteViewModel;
    RVAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DataInsertActivity.class);
                intent.putExtra("type", "addNote");
                startActivityForResult(intent, 1);
            }
        });

        noteViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(NoteViewModel.class);
        noteViewModel.getListLiveData().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                rvAdapter.submitList(notes);
            }
        });

        binding.RV.setLayoutManager(new LinearLayoutManager(this));
        binding.RV.setHasFixedSize(true);
        rvAdapter = new RVAdapter();
        binding.RV.setAdapter(rvAdapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.RIGHT) {
                    noteViewModel.delete(rvAdapter.getNote(viewHolder.getAdapterPosition()));
                    Toast.makeText(MainActivity.this, "note deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, DataInsertActivity.class);
                    intent.putExtra("type", "update");
                    intent.putExtra("tittle", rvAdapter.getNote(viewHolder.getAdapterPosition()).getTitle());
                    intent.putExtra("disc", rvAdapter.getNote(viewHolder.getAdapterPosition()).getDescription());
                    intent.putExtra("id", rvAdapter.getNote(viewHolder.getAdapterPosition()).getId());
                    startActivityForResult(intent, 2);
                    Toast.makeText(MainActivity.this, "note updated", Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(binding.RV);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String tittle = data.getStringExtra("title");
            String description = data.getStringExtra("disc");
            Note note = new Note(tittle, description);
            noteViewModel.insert(note);
            Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();
        } else if (requestCode == 2) {
            String tittle = data.getStringExtra("title");
            String description = data.getStringExtra("disc");
            Note note = new Note(tittle, description);
            note.setId(data.getIntExtra("id", 0));
            noteViewModel.update(note);
            Log.e("SecondResult", "onActivityResult: second");
        }
    }
}