public interface HazardType {
    String getTypeName();
    String getImpactString(double impactValue);
    double applyImpact(double base, double impactValue);
}
