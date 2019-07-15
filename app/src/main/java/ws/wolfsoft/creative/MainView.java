package ws.wolfsoft.creative;

import beans.Note;

import java.util.List;

public interface MainView {
    void showLoading();
    void hideLoading();
    void onGetResult(List<Note> noter);
    void onErrorLoading(String message);
}
