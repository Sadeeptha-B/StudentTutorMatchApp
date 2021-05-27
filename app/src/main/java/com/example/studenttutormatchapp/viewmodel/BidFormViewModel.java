package com.example.studenttutormatchapp.viewmodel;

import android.os.Trace;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.Transformations;

import com.example.studenttutormatchapp.helpers.BidAdditionalInfo;
import com.example.studenttutormatchapp.helpers.Offer;
import com.example.studenttutormatchapp.model.pojo.Bid;
import com.example.studenttutormatchapp.model.pojo.Competency;
import com.example.studenttutormatchapp.model.pojo.Subject;
import com.example.studenttutormatchapp.model.pojo.User;
import com.example.studenttutormatchapp.model.repositories.BidRepository;
import com.example.studenttutormatchapp.model.repositories.UserRepository;
import com.example.studenttutormatchapp.remote.response.ApiResource;
import com.google.gson.Gson;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

import javax.inject.Inject;

public class BidFormViewModel extends CommonViewModel{
    private BidRepository bidRepository;

    private LiveData<ApiResource<User>> userWithSubjects = new MutableLiveData<>();
    private MutableLiveData<Bid> bid = new MutableLiveData<>();
    private LiveData<ApiResource<Bid>> bidResponse = Transformations.switchMap(bid, (bid)->{
        return bidRepository.createBid(bid);
    });

    private List<Competency> competencies = new ArrayList<>();

    @Inject
    public BidFormViewModel(UserRepository userRepository, BidRepository bidRepository) {
        super(userRepository);
        this.bidRepository = bidRepository;
        userWithSubjects = userRepository.getUserWithSubjects();
    }

    public List<String> getSubjectStrings(User data){
        competencies = data.getCompetencies();
        List<String> subjectStrings = new ArrayList<>();
        for (int i = 0; i < competencies.size(); i++){
            Subject subject = competencies.get(i).getSubject();
            subjectStrings.add(subject.getName() + " | " + subject.getDescription());
        }
        return subjectStrings;
    }

    public void getBidData(HashMap<String, String> data) {
        ZonedDateTime dateOpened = ZonedDateTime.now();
        String dateOpenedStr = dateOpened.format(DateTimeFormatter.ISO_INSTANT);
        String dateClosedStr = dateOpened.plus(30, ChronoUnit.MINUTES).format(DateTimeFormatter.ISO_INSTANT);

        BidAdditionalInfo bidAdditionalInfo = new BidAdditionalInfo(
                data.get("competency"),
                data.get("preferredDate"),
                data.get("rateType"),
                data.get("preferredRate"),
                new ArrayList<>()
        );

        if (data.get("bidType") == "closed")
            dateClosedStr = dateOpened.plus(1, ChronoUnit.WEEKS).format(DateTimeFormatter.ISO_INSTANT);

        Subject subject = new Gson().fromJson(data.get("subject"), Subject.class);
        bid.setValue(new Bid(data.get("bidType"), getUserData().getUserId(), dateOpenedStr, dateClosedStr, subject, bidAdditionalInfo));
    }

    public LiveData<ApiResource<Bid>> createBid(){
        return bidResponse;
    }

    public List<Competency> getCompetencyList(){
        return competencies;
    }

    public LiveData<ApiResource<User>> getUserWithSubjects() {
        return userWithSubjects;
    }



}
