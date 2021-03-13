import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;

public class Shares {
    private TreeMap<Integer, Integer> mapBid = new TreeMap<>();
    private TreeMap<Integer, Integer> mapAsk = new TreeMap<>();
    private String inputFile;
    private String outputFile;

    Shares(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public void start() {
        try (Scanner input = new Scanner(new File(inputFile));
             PrintStream output = new PrintStream(new FileOutputStream(outputFile))) {

            while (input.hasNextLine()) {
                String line = input.nextLine();
                String[] parts = line.split(","); //split string by character ','

                if (parts.length == 0) {
                    continue;
                } else if (parts[0].equals("u") && parts.length == 4) {
                    update(parts);
                } else if (parts[0].equals("q") && parts.length >= 2) {
                    query(output, parts);
                } else if (parts[0].equals("o") && parts.length == 3) {
                    if (parts[1].equals("buy")) {
                        remove(parts[2], false, mapAsk);
                    } else if (parts[1].equals("sell")) {
                        remove(parts[2], true, mapBid);
                    }
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("Input file is missing");
        }
    }

    private void remove(String sizeString, boolean useMax, TreeMap<Integer, Integer> map) {
        int sizeToConsume;        
        int checkBox = 0;
        String marketOrder;
        if (useMax) {
        	marketOrder = "sold";  
        } else {
        	marketOrder ="bought"; 
        }
        
        try {
            sizeToConsume = Integer.parseInt(sizeString);
        } catch (NumberFormatException e) {
            return;
        }

        while (sizeToConsume > 0) {
            Map.Entry<Integer, Integer> best = findBestEntry(map, useMax);
            if (best == null) {            	
            	if (checkBox == 0) {
            		System.out.println("not executed: " + sizeToConsume + " share(s) not "+ marketOrder);	
            	}
            	else if (checkBox == 1) {
            		System.out.println("partly executed, " + sizeToConsume + " share(s) still not " + marketOrder);	
            	}             	          	
                break;
            }
            checkBox = 1;
            int price = best.getKey();
            int size = best.getValue();
            if (size >= sizeToConsume) {
                int result = size - sizeToConsume;
                map.put(price, result); 
                sizeToConsume = 0;
            } else {
                sizeToConsume -= size; 
                map.remove(price); 
            }
        }
    }

    private void query(PrintStream output, String[] parts) {
        if (parts[1].equals("best_bid")) {
            printBest(output, mapBid, true);
        } else if (parts[1].equals("best_ask")) {
            printBest(output, mapAsk, false);
        } else if (parts[1].equals("size") && parts.length == 3) {
            try {
                int qPrice = Integer.parseInt(parts[2]);
                if (mapAsk.containsKey(qPrice)) {
                    output.println(mapAsk.get(qPrice));
                }
                if (mapBid.containsKey(qPrice)) {
                    output.println(mapBid.get(qPrice));
                }
            } catch (NumberFormatException e) {
                //System.out.println("query error: " + parts[0] + "," + parts[1] + "," + parts[2]);
            }
        }
    }

    private void printBest(PrintStream output, TreeMap<Integer, Integer> map, boolean useMax) {
        Map.Entry<Integer, Integer> best = findBestEntry(map, useMax);

        if (best != null) {
            output.println(best.getKey() + "," + best.getValue()); 
        }
    }

    private Map.Entry<Integer, Integer> findBestEntry(TreeMap<Integer, Integer> map, boolean useMax) {
        if (!useMax) {
            return getFirstNonZero(map);
        }
        else {
            return getFirstNonZero(map.descendingMap());
        } 
    }

    private Map.Entry<Integer, Integer> getFirstNonZero(NavigableMap<Integer, Integer> map) {
        Map.Entry<Integer, Integer> best = null;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() > 0) {
                best = entry;
                break;
            }
        }
        return best;
    }

    private void update(String[] parts) {
        try {
            if (parts[3].equals("bid")) {
                int keyBid = Integer.parseInt(parts[1]);
                int valueBid = Integer.parseInt(parts[2]);
                if (keyBid > 0 && valueBid >= 0) {
                    mapBid.put(keyBid, valueBid);
                }

            } else if (parts[3].equals("ask")) {
                int keyAsk = Integer.parseInt(parts[1]);
                int valueAsk = Integer.parseInt(parts[2]);
                if (keyAsk > 0 && valueAsk >= 0) {
                    mapAsk.put(keyAsk, valueAsk);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("update error: " + parts[1] + "," + parts[2] + "," + parts[3]);
        }
    }

    public static void main(String[] args) {
        if (args.length == 2) {
            //long ms = System.currentTimeMillis();
        	Shares shares = new Shares(args[0], args[1]);
            shares.start();
            //ms = System.currentTimeMillis() - ms;
            //System.out.println("Elapsed: " + ms + " ms");
        } else {
        	if (args.length < 2) {
        		System.out.println("Error: argument(s) is/are missing");
            } else if (args.length > 2){
            	System.out.println("Error: too many arguments");
            }
        	System.out.println("USAGE: Shares <input file> <output file>"); 
        }
    }
}
