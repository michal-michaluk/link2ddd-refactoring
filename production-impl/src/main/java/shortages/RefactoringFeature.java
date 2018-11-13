package shortages;

public enum RefactoringFeature {
    RUN_NEW_SHORTAGE_FINDER,
    RETURN_NEW_SHORTAGE_FINDER;

    public boolean isActive() {
        return false;
    }
}
