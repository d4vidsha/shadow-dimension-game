public interface Attacker {
    
    /**
     * Inflict damage to the target.
     * @param target Target to inflict damage to.
     */
    void inflictDamageTo(Targetable target);
    
    /**
     * Attack the target.
     */
    void attack();
}
