package com.eclatsol.mynotemvvm;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class NoteRepository {

    //Jyare abstract class hoi tyare objct create karvani jarur nathi
    //Class na name thi variable ,function access kari shako shavo
    //Application context ni sub class hoi che
    //Repository class ni andar apne badha function  bind karvi shavi

    private NoteDao noteDao;
    private LiveData<List<Note>> listLiveData;

    public NoteRepository(Application application) {
        noteDao = NoteDataBase.getInstance(application).getNoteDao();
        listLiveData = noteDao.getAllData();
    }

    public void insertData(Note note) {
        new InsertTask(noteDao).execute(note);
    }

    public void updateData(Note note) {
        new UpdateTask(noteDao).execute(note);
    }

    public void deleteData(Note note) {
        new DeleteTask(noteDao).execute(note);
    }

    public LiveData<List<Note>> getListLiveData() {
        return listLiveData;
    }

    private static class InsertTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        public InsertTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        public DeleteTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    private static class UpdateTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        public UpdateTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

}
