package app.outofthenest.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import app.outofthenest.api.ApiService;
import app.outofthenest.api.UserApi;
import app.outofthenest.models.User;
import app.outofthenest.api.request.LoginRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private UserApi userApi;

    public UserRepository() {
        userApi = ApiService.getRetrofit().create(UserApi.class);
    }

    public LiveData<User> createUser(User user) {
        MutableLiveData<User> data = new MutableLiveData<>();
        userApi.createUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<User> getUser(String id) {
        MutableLiveData<User> data = new MutableLiveData<>();
        userApi.getUser(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                data.setValue(response.body());
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<User> login(String email, String password) {
        MutableLiveData<User> data = new MutableLiveData<>();
        LoginRequest request = new LoginRequest(email, password);
        userApi.login(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                data.setValue(response.body());
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}