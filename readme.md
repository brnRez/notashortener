# Not a shortener - a simple url shortener project

Following the steps proposed in the [back-end br repository](https://github.com/backend-br/desafios/blob/master/url-shortener/PROBLEM.md) i was able to challenge myself on enhancing my skills while developing this project.

## Functionalities

- Generates a shortened Url based on the user's provided one;
- Checks if the provided Url contains the "http" prefix, and if it doesn't, adds it;
- Alongside the generated shortened Url, provides a QR Code generated from it as well;
- When the user access the shortened Url, it redirects him to the original Url provided before.


## Tools Used

- Java 17, base language
- Spring Framework, along with its tools (Lombok, JPA and WebTools)
- SQL Database: PostgreSQL for data storaging;
- Docker Compose, containing the postgres latest version image;
- Apache Commons Lang3, for generating a random string to build the shortened url;
- Google ZXing, for QRCode generation.
- AWS S3 Client, for storaging.

## Endpoints

- `/do-not-shorten` : to provide the selected url for shortening;
- `/r/{shortenedUrl}` : for redirecting the user to the original Url based on the shortened string;
- `/qr-codes/{shortenedUrl}` : Deprecated, as the API now uses Cloud Storaging.

## Usage

To test it, after successfully running the DB from the docker-compose file, you can send a POST request through your preferred tool
providing a JSON file with the "originalUrl" attribute as a payload. By that, the response should be the generated info (Shortened Url, as well as the generated QR Code).

I've recorded a quick showcase on how the API works, you can watch it by [clicking here](https://youtu.be/vwzGJwOsxRE)   

## Insights

As this project was made only for studying purposes, it has reached its objectives, as i've acquired some experience with containerization tools (docker), had my first contact with the AWS platform as well as successfully installed its CLI app. In conclusion, this project gave me an overall knowledge in programming logic, Regex, cloud tools and design patterns. Can't wait to jump into the next one! 