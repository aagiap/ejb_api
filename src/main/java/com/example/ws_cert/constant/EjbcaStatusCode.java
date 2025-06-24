package com.example.ws_cert.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum EjbcaStatusCode {
    EJBCA_ERROR(199, "Error from ejbca server", "Error from ejbca server"),
    OK(200, "OK", "Successful HTTP request, expect a proper response body. Typically received from a successful GET request."),
    CREATED(201, "Created", "Successful HTTP request. Some entity has been created, e.g. certificate enrolled. Typically received after a successful POST request."),
    ACCEPTED(202, "Accepted", "Request accepted by the server but awaiting processing. Most likely waiting for administrator approval."),
    BAD_REQUEST(400, "Bad Request", "Invalid input parameters / JSON body in the request, invalid alias, non-existing CA, or pre-condition failed for certificate creation or renewal."),
    FORBIDDEN(403, "Forbidden", "Request accepted but the operation is refused due to insufficient privileges, disabled features etc."),
    NOT_FOUND(404, "Not Found", "Requested entity was not found by EJBCA. Could occur if the input refers to a non-existing entity, such as a user or a CA."),
    CONFLICT(409, "Conflict", "Conflict occurred while processing request, e.g. trying to revoke an already revoked certificate."),
    PAYLOAD_TOO_LARGE(413, "Payload Too Large", "The request is larger than the server is willing to process. Should not occur while using the API as intended."),
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity", "Well-formed request but unable to process due to semantic errors. Could occur in case of invalid key algorithm, validity etc."),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", "Unexpected error while calling the API. For additional details, refer to the server log."),
    SERVICE_UNAVAILABLE(503, "Service Unavailable", "Possible reason could be that the CA is offline, CT log is unavailable etc."),
    ;

    private final int code;
    private final String message;
    private final String description;

    public static EjbcaStatusCode fromCode(int code) {
        return Arrays.stream(EjbcaStatusCode.values())
                .filter(status -> status.getCode() == code)
                .findFirst()
                .orElse(EJBCA_ERROR);
    }

}
