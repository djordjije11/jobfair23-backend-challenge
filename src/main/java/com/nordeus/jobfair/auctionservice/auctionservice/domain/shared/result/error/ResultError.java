package com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result.error;

public record ResultError(
        ErrorCode code,
        String message
) {
    public static <T> ResultError notFound(Class<T> clazz, String identifierName, String identifier)
    {
        return new ResultError(
                ErrorCode.NotFound,
                String.format("%s with %s: %s is not found.", clazz.getSimpleName(), identifierName, identifier)
        );
    }

    public static <T> ResultError notFound(Class<T> clazz, String identifier)
    {
        return notFound(clazz, "identifier", identifier);
    }

    public static ResultError badRequest(String message)
    {
        return new ResultError(ErrorCode.BadRequest, message);
    }
}
