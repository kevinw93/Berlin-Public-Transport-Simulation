public class ShortTermHazardType implements HazardType {
    @Override
    public String getTypeName() {
        return "SHORT_TERM";
    }
    
    @Override
    public String getImpactString(double impactValue) {
        return String.format("+%.2f minutes", impactValue);
    }
    
    @Override
    public double applyImpact(double base, double impactValue) {
        return base + impactValue;
    }
} 