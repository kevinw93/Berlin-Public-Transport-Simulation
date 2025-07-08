public class LongTermHazardType implements HazardType {
    @Override
    public String getTypeName() {
        return "LONG_TERM";
    }
    
    @Override
    public String getImpactString(double impactValue) {
        return String.format("x%.2f duration", impactValue);
    }
    
    @Override
    public double applyImpact(double base, double impactValue) {
        return Math.ceil(base * impactValue);
    }
} 