package com.example.nghenhac;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FavoriteSongActivity extends AppCompatActivity {
    SongAdapter adapter;
    Button back;
    ListView listViewFavorites;
    List<Song> allSongs = new ArrayList<>(); // Danh sách tất cả bài hát
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_song);

        // Khởi tạo và tải danh sách yêu thích từ SharedPreferences
        FavoriteSongs.initialize(this); // Gọi phương thức initialize để tải danh sách yêu thích

        // Tải danh sách yêu thích
        back = findViewById(R.id.back);
        back.setOnClickListener(view -> finish());
        listViewFavorites = findViewById(R.id.listViewFavorites);

        adapter = new SongAdapter(this, FavoriteSongs.getFavoriteSongs(),
                (song, position) -> {
                    List<Song> favoriteSongs = FavoriteSongs.getFavoriteSongs();
                    Intent intent = new Intent(FavoriteSongActivity.this, MusicPlayerActivity.class);
                    intent.putExtra("currentSongIndex", position);
                    intent.putExtra("favoriteSongs", (Serializable) favoriteSongs);
                    intent.putExtra("isFavoriteMode", true);
                    startActivity(intent);
                },
                song -> {
                    removeFromFavorites(song);
                });

        listViewFavorites.setAdapter(adapter);
    }

    private void removeFromFavorites(Song song) {
        // Xóa bài hát khỏi danh sách yêu thích
        FavoriteSongs.removeFavorite(song, this);

        // Hiển thị thông báo
        Toast.makeText(this, "Đã xóa khỏi mục yêu thích!", Toast.LENGTH_SHORT).show();

        // Cập nhật lại danh sách
        adapter.notifyDataSetChanged();
    }
    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged(); // Cập nhật lại adapter
    }
}