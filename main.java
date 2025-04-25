import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {
    public static void main(String[] args) {
        try {
            String[] testFiles = {"testcase1.json", "testcase2.json"};

            for (int t = 0; t < testFiles.length; t++) {
                System.out.println("Processing " + testFiles[t]);

                JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(testFiles[t]));

                JSONObject keysObj = (JSONObject) jsonObject.get("keys");
                int k = Integer.parseInt(keysObj.get("k").toString());

                Map<Integer, Double> points = new TreeMap<>();

                for (Object key : jsonObject.keySet()) {
                    if (key.equals("keys")) continue;

                    int x = Integer.parseInt(key.toString());
                    JSONObject point = (JSONObject) jsonObject.get(key);
                    int base = Integer.parseInt(point.get("base").toString());
                    String val = point.get("value").toString();

                    BigInteger yBig = new BigInteger(val, base);
                    double y = yBig.doubleValue();

                    points.put(x, y);

                    if (points.size() == k) break;
                }

                List<Double> xVals = new ArrayList<>();
                List<Double> yVals = new ArrayList<>();

                for (Map.Entry<Integer, Double> entry : points.entrySet()) {
                    xVals.add((double) entry.getKey());
                    yVals.add(entry.getValue());
                }

                double secret = lagrangeInterpolation(xVals, yVals, 0.0);
                System.out.println("Secret for " + testFiles[t] + " (c): " + Math.round(secret));
                System.out.println();
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static double lagrangeInterpolation(List<Double> x, List<Double> y, double xVal) {
        double result = 0.0;
        int n = x.size();

        for (int i = 0; i < n; i++) {
            double term = y.get(i);
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    term *= (xVal - x.get(j)) / (x.get(i) - x.get(j));
                }
            }
            result += term;
        }

        return result;
    }
}
