public class worker extends Thread {

    int id;
    CollisionResistance cResistance;
    byte[] digest;

    public worker(int id, byte[] digest, CollisionResistance cResistance) {
        this.id = id;
        this.digest = digest;
        this.cResistance = cResistance;
    }

    public void run() {

        while(cResistance.finished == false) {
            cResistance.bruteForce(digest, id);
        }
        cResistance.finished = true;

    }
}