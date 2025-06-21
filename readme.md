# Not a shortener - a simple url shortener project

Following the steps proposed in the [back-end br repository](https://github.com/backend-br/desafios/blob/master/url-shortener/PROBLEM.md) i was able to challenge myself on enhancing my skills while developing this project.

## Functionalities

- Generates a shortened Url based on the user's provided one;
- Checks if the provided Url contains the "http" prefix, and if it doesn't, adds it;
- Alongside the generated shortened Url, it provides a QR Code containing it too;
- When the user access the shortened Url, it redirects him to the original Url provided before.


## Tools Used

- SQL Database: PostgreSQL for data storaging;
- Docker Compose, containing the postgres latest version image;
- Apache Commons Lang3, for generating a random string to build the shortened url;
- Google ZXing, for QRCode generation.

## Endpoints

- `/donotshorten` : to provide the selected url for shortening;
- `/r/{shortenedUrl}` : for redirecting the user to the original Url based on the shortened string;
- `/qr-codes/{shortenedUrl}` : to provide the shortened Url QR Code image download.