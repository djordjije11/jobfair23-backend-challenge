package com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result.error.ResultError;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result.error.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Result {
    private final boolean isError;
    private final ResultError error;

    public static Result success() {
        return new Result(false, null);
    }

    public static Result failure() {
        return new Result(true, null);
    }

    public static Result failure(ErrorCode code, String message) {
        return new Result(true, new ResultError(code, message));
    }

    public static Result failure(ResultError error) {
        return new Result(true, error);
    }
}

