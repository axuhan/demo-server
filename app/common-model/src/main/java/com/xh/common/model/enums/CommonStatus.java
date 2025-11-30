package com.xh.common.model.enums;

public enum CommonStatus {

    NORMAL(0),
    DELETED(1)
    ;

    private final int number;

    CommonStatus(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public static CommonStatus getByNumber(int number) {
        for (CommonStatus status : CommonStatus.values()) {
            if (status.getNumber() == number) {
                return status;
            }
        }
        return null;
    }

    public interface CommonStatusConverter {
        default CommonStatus commonStatusFromNumber(Integer number) {
            return number == null ? null : CommonStatus.getByNumber(number);
        }

        default Integer commonStatusToNumber(CommonStatus status) {
            return status == null ? null : status.getNumber();
        }
    }
}
