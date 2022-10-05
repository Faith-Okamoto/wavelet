import java.io.IOException;
import java.net.URI;
// used for storing unique strings
import java.util.HashSet;

class Handler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    HashSet<String> library = new HashSet<>();

    public String handleRequest(URI url) {
        // default page is the available library
        if (url.getPath().equals("/")) {
            return "Current string library to search through:\n" + library.toString();
        // /add page adds a string to the library
        } else if (url.getPath().equals("/add")) {
            String query = url.getQuery();
            // only accept properly formatted queries
            if (query != null && query.split("=").length == 2 && 
                query.split("=")[0].equals("s")) {
                String word = query.split("=")[1];
                // only add new words if necessary
                if (library.contains(word)) {
                    return "Library already contains '" + word + "'.";
                }
                else {
                    library.add(word);
                    return "Added '" + word + "' to library";
                }
            }
            // warn user that their query was not accepted
            return "To add a word to the library, use a query such as '?s=cat'. Your query: " + query;
        // /search page adds a string to the library
        } else if (url.getPath().equals("/search")) {
            String query = url.getQuery();
            // only accept properly formatted queries
            if (query != null && query.split("=").length == 2 && 
                query.split("=")[0].equals("s")) {
                String search = query.split("=")[1];
                // for storing any string matches found in the library
                HashSet<String> found = new HashSet<>();
                for (String word : library) {
                    if (word.contains(search)) {found.add(word);}
                }
                // pretty-print results
                if (found.isEmpty()) {
                    return "No matches found in library for '" + search + "'.";
                } else {
                    return "Matches found for '" + search + "':\n" + found.toString();
                }
            }
            // warn user that their query was not accepted
            return "To search the library, use a query such as '?s=cat'. Your query: " + query;
        // any other page 404s
        } else {
            return "404 Not Found!";
        }
    }
}

class SearchEngine {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}
