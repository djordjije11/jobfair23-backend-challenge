package com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.timer;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.LiveAuction;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class LiveAuctionTimer extends TimerTask {
    @Getter
    @EqualsAndHashCode.Include
    @NonNull
    private final LiveAuction liveAuction;
    @Getter
    private final LiveAuctionTimerSettings settings;
    @Getter
    private int remainingDurationInSeconds;

    private final Timer timer = new Timer();

    private static final int TASK_EXECUTION_DELAY = 0;
    private static final int TASK_EXECUTION_RATE = 1000;

    public LiveAuctionTimer(@NonNull LiveAuction liveAuction) {
        this(liveAuction, new LiveAuctionTimerSettings(60, 5));
    }

    public LiveAuctionTimer(@NonNull LiveAuction liveAuction, LiveAuctionTimerSettings settings) {
        this.liveAuction = liveAuction;
        liveAuction.setLiveAuctionTimer(this);
        this.settings = settings;
        this.remainingDurationInSeconds = settings.durationInSeconds();
    }

    @Override
    public void run() {
        remainingDurationInSeconds--;
        log.info("Auction ID: {}; TIME: {};", this.liveAuction.getAuction().getId(), this.remainingDurationInSeconds);
        if (remainingDurationInSeconds == 0) {
            finish();
        }
    }

    public void start() {
        timer.scheduleAtFixedRate(this, TASK_EXECUTION_DELAY, TASK_EXECUTION_RATE);
    }

    public void finish() {
        timer.cancel();
        liveAuction.finish();
    }

    public void bidPlaced() throws LiveAuctionFinishedException {
        if (remainingDurationInSeconds == 0) {
            throw new LiveAuctionFinishedException();
        }
        refreshDuration();
    }

    private void refreshDuration() {
        if (remainingDurationInSeconds >= settings.bidAddedRefreshTimeInSeconds()) {
            return;
        }
        remainingDurationInSeconds = settings.bidAddedRefreshTimeInSeconds();
    }
}
