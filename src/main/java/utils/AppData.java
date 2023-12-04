package utils;


public class AppData {

    public enum Provider {
        LOCAL("local"),
        BROWSERSTACK("browserstack"),
        REMOTE("remote");
        private final String _Provider;

        Provider(String Provider) {
            this._Provider = Provider;
        }

        public String getProvider() {
            return _Provider;
        }
    }
}


