package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import org.junit.jupiter.api.Test;

class AddressTest {

  @Test
  void getNearbyStoresTest() {
    Address address = new Address();
    address.setStreet("100 Insitiute Road");
    address.setCity("Worcester");
    address.setRegion("MA");
    address.setCountry("USA");
    address.setZip("01609");

    //System.out.println(address.getNearbyStores());
  }
}
