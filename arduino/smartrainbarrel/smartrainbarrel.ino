#include <SPI.h>
#include <Ethernet.h>
#include "HX711.h"


#define calibration_factor 25000.0
#define DOUT  3
#define CLK  2
HX711 scale(DOUT, CLK);

byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
char server[] = "api-m2x.att.com";
IPAddress ip(192, 168, 0, 177);
EthernetClient client;

const float minDeta = 2;
float lastWaterWeight = 0;

int lastValvePoll = 0;

void setup() {
  // Open serial communications and wait for port to open:
  Serial.begin(9600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }

  scale.set_scale(calibration_factor);
  scale.tare();

  if (Ethernet.begin(mac) == 0) {
    Serial.println("Failed to configure Ethernet using DHCP");
    // try to congifure using IP address instead of DHCP:
    Ethernet.begin(mac, ip);
  }
  // give the Ethernet shield a second to initialize:
  delay(1000);
  Serial.println("connecting...");

}

void loop() {
	//check water level
	float currentWaterWeight = scale.get_units();
	float delta = abs( ( lastWaterWeight - currentWaterWeight ) / lastWaterWeight ) * 100 )

	if (delta > minDelta) {
		sendWaterLevel(currentWaterWeight);
		lastWaterweight = currentWaterWeight;
	}

	

	
}

bool checkOpenValve() {
  // if you get a connection, report back via serial:
  if (client.connect(server, 80)) {
    Serial.println("connected");
    // Make a HTTP request:
    client.println("GET /v2/devices/c18123714d4993d81cf0da9b0f1d2446/streams/valve/value HTTP/1.1");
    client.println("Host: api-m2x.att.com");
    client.println("Connection: close");
    client.println();
  } else {
    // if you didn't get a connection to the server:
    Serial.println("connection failed");
  }
  // if there are incoming bytes available
  // from the server, read them and print them:
  while (true) {
		if (client.available()) {
			char c = client.read();
			Serial.print(c);
		}

		// if the server's disconnected, stop the client:
		if (!client.connected()) {
			Serial.println();
			Serial.println("disconnecting.");
			client.stop();
			return;
		}

  }

}

void sendWaterLevel(float weight) {
  // if you get a connection, report back via serial:
  if (client.connect(server, 80)) {
    Serial.println("connected");
    // Make a HTTP request:
    client.println("PUT /v2/devices/c18123714d4993d81cf0da9b0f1d2446/streams/weight/value HTTP/1.1");
    client.println("Host: api-m2x.att.com");
    client.println("Connection: close");
    client.println();
  } else {
    // if you didn't get a connection to the server:
    Serial.println("connection failed");
  }
  // if there are incoming bytes available
  // from the server, read them and print them:
  while (true) {
		if (client.available()) {
			char c = client.read();
			Serial.print(c);
		}

		// if the server's disconnected, stop the client:
		if (!client.connected()) {
			Serial.println();
			Serial.println("disconnecting.");
			client.stop();
			return;
		}

  }
}

