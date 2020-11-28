package com.example.project3.utils;

import android.content.Context;
import android.util.Log;


import com.example.project3.model.MyPlaylistModel;
import com.example.project3.model.SongModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.example.project3.utils.Static.listmyplaylist;

public class RealmHelper {

    Realm realm;
    Context contex;
    boolean isexist =false;
    public RealmHelper(Context contex) {
        Realm.init(contex);
        RealmConfiguration configuration = new RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
        realm = Realm.getInstance(configuration);
        this.contex = contex;
    }
    public interface MyRealmListener {
        void onsuccess();
    }
    private  MyRealmListener listener;

    public void setMyRealmListener(MyRealmListener listener) {
        this.listener = listener;
    }



    // untuk menyimpan data
    public void creatnewplaylist(final MyPlaylistModel newmodelPlaylist) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                    if (realm != null) {
                        MyPlaylistModel modelPlaylist = realm.where(MyPlaylistModel.class)
                                .sort("id", Sort.DESCENDING)
                                .findFirst();
                            if (modelPlaylist==null){
                                newmodelPlaylist.setId(2);
                            }
                            else {
                                newmodelPlaylist.setId(modelPlaylist.getId()+1);
                            }
                        try {
                            MyPlaylistModel model = realm.copyToRealm(newmodelPlaylist);
                            System.out.println("model created"+newmodelPlaylist.getId());
                        } catch (Exception e) {
                        }


                    } else {
                        Log.e("ppppp", "execute: Database not Exist");
                    }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                listener.onsuccess();
            }
        });
    }

    // untuk menyimpan data recent
    public void saverecent(final SongModel songModel) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (realm != null) {
                    RealmResults<SongModel> result = realm.where(SongModel.class)
                            .equalTo("id", songModel.getId())
                            .equalTo("playlistid", "0")
                            .findAll();

                    if (result.size() == 0) {
                        try {
                            songModel.setIndex(checkindex());
                            songModel.setPlaylistid("0");
                            SongModel model = realm.copyToRealm(songModel);
                            System.out.println("ditambahkan ke recent");
                        } catch (Exception e) {
//                        Toast.makeText(contex,"Song already exists",Toast.LENGTH_LONG).show();

                        }
                    }
                } else {
                }
            }
        });
    }

    public boolean checkIsFav(int idsong){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<SongModel> result = realm.where(SongModel.class)
                        .equalTo("id", idsong)
                        .equalTo("playlistid", "1")
                        .findAll();
                isexist= result.size() > 0;
            }
        });

        Log.e("checkIsFav", "checkIsFav: "+isexist );

        return isexist;

    }

    // untuk menyimpan data
    public void actionfav(final SongModel songModel,boolean delete) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (realm != null) {
                    SongModel newsongmodel = realm.where(SongModel.class)
                            .equalTo("id", songModel.getId())
                            .equalTo("playlistid", "1")
                            .findFirst();

                    if (newsongmodel !=null) {
                        if (delete){
                            newsongmodel.deleteFromRealm();
                        }

                    } else {
                        if (!delete){
                            addtonewplaylist(songModel,"1",realm);
                        }
                    }
                } else {
                    Log.e("ppppp", "execute: Database not Exist");
                }
            }
        });
    }


    // untuk menyimpan lagu ke playlist
    public void saveplaylists(final SongModel songModel,String playlistid) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (realm != null) {
                    RealmResults<SongModel> result = realm.where(SongModel.class)
                            .equalTo("id", songModel.getId())
                            .equalTo("playlistid", playlistid)
                            .findAll();
                    if (result.size() == 0) {
                        addtonewplaylist(songModel,playlistid,realm);

                    }
                    else {
                        Log.e("onError", "sudah ada "+result.size());

                    }



                } else {
                    Log.e("ppppp", "execute: Database not Exist");
                }
            }

        });
    }

    public List<SongModel> getAllSongsrecent() {
        RealmResults<SongModel> results = realm.where(SongModel.class)
                .equalTo("playlistid", "0")
                .findAll();
        return results;
    }
    public List <SongModel> getSongsbyPlaylistid(String playlistId) {

        RealmResults<SongModel> results = realm.where(SongModel.class)
                .equalTo("playlistid", playlistId)
                .findAll();
        return results;

    }





    public  List<MyPlaylistModel>  getallPlaylist() {

        RealmResults<MyPlaylistModel> results = realm.where(MyPlaylistModel.class)
                .findAll();
        return results;

    }







    public void updateplaylisttotalsong(int playlistid,boolean status) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MyPlaylistModel model = realm.where(MyPlaylistModel.class)
                        .equalTo("id", playlistid)
                        .findFirst();

                assert model != null;
                if (status){
                    model.setTotalsong(model.getTotalsong()+1);

                }
                else {
                    model.setTotalsong(model.getTotalsong()-1);

                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                listener.onsuccess();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }


    // untuk menghapus data
    public void delete(Integer id,String playlistid) {
        final RealmResults<SongModel> model = realm.where(SongModel.class).equalTo("id", id).equalTo("playlistid", playlistid).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                model.deleteFromRealm(0);
                updateplaylisttotalsong(Integer.parseInt(playlistid),false);


            }
        });
    }
    public void deletesongByid(String playlistid) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                final RealmResults<SongModel> model = realm.where(SongModel.class).equalTo("playlistid", playlistid).findAll();
                realm.beginTransaction();
                model.deleteAllFromRealm();
                realm.commitTransaction();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
            }
        });

    }
    public void deletePlaylist(int playlistid) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                MyPlaylistModel playlist = bgRealm.where(MyPlaylistModel.class)
                        .equalTo("id", playlistid)
                        .findFirst();
                playlist.deleteFromRealm();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                listener.onsuccess();
                // Transaction was a success.
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("onError", "onError: "+error );
                // Transaction failed and was automatically canceled.
            }
        });
    }

    int checkindex(){
        Number currentIdNum = realm.where(SongModel.class).max("index");
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return  nextId;
    }

     void addtonewplaylist (SongModel songModel, String playlistid, Realm realm){
                 try {
                     SongModel newmodel= new SongModel();
                     newmodel.setId(songModel.getId());
                     newmodel.setTitle(songModel.getTitle());
                     newmodel.setImageurl(songModel.getImageurl());
                     newmodel.setArtist(songModel.getArtist());
                     newmodel.setAlbum(songModel.getAlbum());
                     newmodel.setGenre(songModel.getGenre());
                     newmodel.setLyric(songModel.getLyric());
                     newmodel.setSongurl(songModel.getSongurl());
                     newmodel.setYears(songModel.getYears());
                     newmodel.setDuration(songModel.getDuration());
                     newmodel.setIndex(checkindex());
                     newmodel.setPlaylistid(playlistid);

                     realm.copyToRealm(newmodel);

                     updateplaylisttotalsong(Integer.parseInt(playlistid),true);
//
                     Log.e("onError", "songid "+newmodel.getId());
                 } catch (Exception e) {

                 }

    }

}