# Officer send rest to issue fine
import requests
import json

# Define the base URL for the REST service
BASE_URL = "http://localhost:9093"

# Endpoint to test
ENDPOINT = "/issue/"

# Define the test payload
payload = {
    "licensePlate": "squeeze",
    "fineAmount": "100"
}

# Send the POST request to the endpoint
response = requests.post(
    url=f"{BASE_URL}{ENDPOINT}",
    headers={"Content-Type": "application/json"},
    data=json.dumps(payload)
)

# Validate the response
if response.status_code == 202:
    print("Response body:", response.text)
else:
    print(f"HTTP Status Code: {response.status_code}")
    print("Response body:", response.text)
