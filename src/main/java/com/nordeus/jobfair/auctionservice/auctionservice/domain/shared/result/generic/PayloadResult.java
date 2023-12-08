package com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result.generic;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result.error.ErrorCode;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result.error.ResultError;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PayloadResult<T> {
    private final boolean isError;
    private final ResultError error;
    private final T payload;

    public static <T> PayloadResult<T> map(com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result.Result result) {
        return new PayloadResult<T>(result.isError(), result.getError(), null);
    }

    public static <T> PayloadResult<T> success() {
        return new PayloadResult<T>(false, null, null);
    }

    public static <T> PayloadResult<T> success(T payload) {
        return new PayloadResult<T>(false, null, payload);
    }

    public static <T> PayloadResult<T> failure() {
        return new PayloadResult<T>(true, null, null);
    }

    public static <T> PayloadResult<T> failure(ErrorCode code, String message) {
        return new PayloadResult<>(true, new ResultError(code, message), null);
    }

    public static <T> PayloadResult<T> failure(ResultError error) {
        return new PayloadResult<T>(true, error, null);
    }
}
