package tl.alex.common;


import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApiErrors {

    private List<ApiError> errors;

    public ApiErrors() {

        errors = new ArrayList<>();
    }

    public ApiErrors(List<ApiError> errors) {
        this.errors = errors;
    }

    public void setError(ApiError error) {
        errors.add(error);
    }

    public static ApiErrors get(ApiError error) {
        ApiErrors apiErrors = new ApiErrors();
        apiErrors.setError(error);
        return apiErrors;
    }

    public static ApiErrors get(List<ApiError> errors) {
        ApiErrors apiErrors = new ApiErrors();
        apiErrors.setErrors(errors);
        return apiErrors;
    }
}
