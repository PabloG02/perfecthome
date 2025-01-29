package com.pablog.perfecthome.service;

import com.pablog.perfecthome.entity.Listing;
import com.pablog.perfecthome.entity.Property;
import com.pablog.perfecthome.repository.ListingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListingServiceTest {

    @InjectMocks
    private ListingService listingService;

    @Mock
    private ListingRepository listingRepository;

    @Captor
    private ArgumentCaptor<Listing> listingCaptor;

    private Property testProperty;

    @BeforeEach
    void setUp() {
        testProperty = new Property(); // Create a test property instance
        testProperty.setId(1L); // Assign an ID for property
    }

    // Helper method to create Listing instances for tests
    private Listing createListing(LocalDateTime publicationDate, LocalDateTime expirationDate) {
        Listing listing = new Listing();
        listing.setProperty(testProperty);
        listing.setPublicationDate(publicationDate);
        listing.setExpirationDate(expirationDate);
        return listing;
    }

    @Test
    void saveListing_Overlap_15thTo25th_Rejected() {
        // Arrange
        LocalDateTime existingStart = LocalDateTime.of(2024, 1, 10, 0, 0); // 2024-01-10 00:00
        LocalDateTime existingEnd = LocalDateTime.of(2024, 1, 20, 0, 0); // 2024-01-20 00:00
        List<Listing> existingListings = Collections.singletonList(createListing(existingStart, existingEnd));
        when(listingRepository.findByProperty(testProperty)).thenReturn(existingListings);

        // From 2024-01-15 00:00 to 2024-01-25 00:00
        Listing newListing = createListing(LocalDateTime.of(2024, 1, 15, 0, 0), LocalDateTime.of(2024, 1, 25, 0, 0));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> listingService.save(newListing));
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Cannot create listing. Publication or Expiration date overlaps with existing active listing for the same property.", exception.getReason());
        verify(listingRepository, never()).save(newListing); // Ensure new listing is not saved
    }

    @Test
    void saveListing_Overlap_5thTo15th_Rejected() {
        // Arrange
        LocalDateTime existingStart = LocalDateTime.of(2024, 1, 10, 0, 0); // 2024-01-10 00:00
        LocalDateTime existingEnd = LocalDateTime.of(2024, 1, 20, 0, 0); // 2024-01-20 00:00
        List<Listing> existingListings = Collections.singletonList(createListing(existingStart, existingEnd));
        when(listingRepository.findByProperty(testProperty)).thenReturn(existingListings);

        // From 2024-01-05 00:00 to 2024-01-15 00:00
        Listing newListing = createListing(LocalDateTime.of(2024, 1, 5, 0, 0), LocalDateTime.of(2024, 1, 15, 0, 0));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> listingService.save(newListing));
        verify(listingRepository, never()).save(newListing);
    }

    @Test
    void saveListing_Overlap_5thToNull_Rejected() {
        // Arrange
        LocalDateTime existingStart = LocalDateTime.of(2024, 1, 10, 0, 0); // 2024-01-10 00:00
        LocalDateTime existingEnd = LocalDateTime.of(2024, 1, 20, 0, 0); // 2024-01-20 00:00
        List<Listing> existingListings = Collections.singletonList(createListing(existingStart, existingEnd));
        when(listingRepository.findByProperty(testProperty)).thenReturn(existingListings);

        // From 2024-01-05 00:00 to null
        Listing newListing = createListing(LocalDateTime.of(2024, 1, 5, 0, 0), null);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> listingService.save(newListing));
        verify(listingRepository, never()).save(newListing);
    }

    @Test
    void saveListing_Overlap_15thTo17th_Rejected() {
        // Arrange
        LocalDateTime existingStart = LocalDateTime.of(2024, 1, 10, 0, 0); // 2024-01-10 00:00
        LocalDateTime existingEnd = LocalDateTime.of(2024, 1, 20, 0, 0); // 2024-01-20 00:00
        List<Listing> existingListings = Collections.singletonList(createListing(existingStart, existingEnd));
        when(listingRepository.findByProperty(testProperty)).thenReturn(existingListings);

        // From 2024-01-15 00:00 to 2024-01-17 00:00
        Listing newListing = createListing(LocalDateTime.of(2024, 1, 15, 0, 0), LocalDateTime.of(2024, 1, 17, 0, 0));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> listingService.save(newListing));
        verify(listingRepository, never()).save(newListing);
    }

    @Test
    void saveListing_NoOverlap_5thTo8th_OK() {
        // Arrange
        LocalDateTime existingStart = LocalDateTime.of(2024, 1, 10, 0, 0); // 2024-01-10 00:00
        LocalDateTime existingEnd = LocalDateTime.of(2024, 1, 20, 0, 0); // 2024-01-20 00:00
        List<Listing> existingListings = Collections.singletonList(createListing(existingStart, existingEnd));
        when(listingRepository.findByProperty(testProperty)).thenReturn(existingListings);

        // From 2024-01-05 00:00 to 2024-01-08 00:00
        Listing newListing = createListing(LocalDateTime.of(2024, 1, 5, 0, 0), LocalDateTime.of(2024, 1, 8, 0, 0));

        // Act
        Listing savedListing = listingService.save(newListing);

        // Assert
        verify(listingRepository, times(1)).save(listingCaptor.capture());
        Listing savedListingArgument = listingCaptor.getValue();
        assertEquals(newListing, savedListingArgument);
    }

    @Test
    void saveListing_NoOverlap_22ndTo25th_OK() {
        // Arrange
        LocalDateTime existingStart = LocalDateTime.of(2024, 1, 10, 0, 0); // 2024-01-10 00:00
        LocalDateTime existingEnd = LocalDateTime.of(2024, 1, 20, 0, 0); // 2024-01-20 00:00
        List<Listing> existingListings = Collections.singletonList(createListing(existingStart, existingEnd));
        when(listingRepository.findByProperty(testProperty)).thenReturn(existingListings);

        // From 2024-01-22 00:00 to 2024-01-25 00:00
        Listing newListing = createListing(LocalDateTime.of(2024, 1, 22, 0, 0), LocalDateTime.of(2024, 1, 25, 0, 0));

        // Act
        Listing savedListing = listingService.save(newListing);

        // Assert
        verify(listingRepository, times(1)).save(listingCaptor.capture());
        Listing savedListingArgument = listingCaptor.getValue();
        assertEquals(newListing, savedListingArgument);
    }

    @Test
    void saveListing_NoOverlap_22ndToNull_OK() {
        // Arrange
        LocalDateTime existingStart = LocalDateTime.of(2024, 1, 10, 0, 0); // 2024-01-10 00:00
        LocalDateTime existingEnd = LocalDateTime.of(2024, 1, 20, 0, 0); // 2024-01-20 00:00
        List<Listing> existingListings = Collections.singletonList(createListing(existingStart, existingEnd));
        when(listingRepository.findByProperty(testProperty)).thenReturn(existingListings);

        // From 2024-01-22 00:00 to null
        Listing newListing = createListing(LocalDateTime.of(2024, 1, 22, 0, 0), null);

        // Act
        Listing savedListing = listingService.save(newListing);

        // Assert
        verify(listingRepository, times(1)).save(listingCaptor.capture());
        Listing savedListingArgument = listingCaptor.getValue();
        assertEquals(newListing, savedListingArgument);
    }

    @Test
    void saveListing_NoOverlap_ExistingNullEnd_15thTo25th_OK() {
        // Arrange
        LocalDateTime existingStart = LocalDateTime.of(2024, 1, 10, 0, 0); // 2024-01-10 00:00
        LocalDateTime existingEnd = null; // No expiration
        List<Listing> existingListings = Collections.singletonList(createListing(existingStart, existingEnd));
        when(listingRepository.findByProperty(testProperty)).thenReturn(existingListings);

        // From 2024-01-15 00:00 to 2024-01-25 00:00
        Listing newListing = createListing(LocalDateTime.of(2024, 1, 15, 0, 0), LocalDateTime.of(2024, 1, 25, 0, 0));

        // Act
        Listing savedListing = listingService.save(newListing);

        // Assert
        verify(listingRepository, times(2)).save(listingCaptor.capture());
        List<Listing> savedListings = listingCaptor.getAllValues();
        assertNotNull(savedListings.get(0).getExpirationDate());
        assertEquals(newListing, savedListings.get(1));
    }

    @Test
    void saveListing_NoOverlap_ExistingNullEnd_15thToNull_OK() {
        // Arrange
        LocalDateTime existingStart = LocalDateTime.of(2024, 1, 10, 0, 0); // 2024-01-10 00:00
        LocalDateTime existingEnd = null; // No expiration
        List<Listing> existingListings = Collections.singletonList(createListing(existingStart, existingEnd));
        when(listingRepository.findByProperty(testProperty)).thenReturn(existingListings);

        // From 2024-01-15 00:00 to null
        Listing newListing = createListing(LocalDateTime.of(2024, 1, 15, 0, 0), null);

        // Act
        Listing savedListing = listingService.save(newListing);

        // Assert
        verify(listingRepository, times(2)).save(listingCaptor.capture());
        List<Listing> savedListings = listingCaptor.getAllValues();
        assertNotNull(savedListings.get(0).getExpirationDate());
        assertEquals(newListing, savedListings.get(1));
    }

    @Test
    void saveListing_NoOverlap_ExistingNullEnd_1stTo5th_OK() {
        // Arrange
        LocalDateTime existingStart = LocalDateTime.of(2024, 1, 10, 0, 0); // 2024-01-10 00:00
        LocalDateTime existingEnd = null; // No expiration
        List<Listing> existingListings = Collections.singletonList(createListing(existingStart, existingEnd));
        when(listingRepository.findByProperty(testProperty)).thenReturn(existingListings);

        // From 2024-01-01 00:00 to 2024-01-05 00:00
        Listing newListing = createListing(LocalDateTime.of(2024, 1, 1, 0, 0), LocalDateTime.of(2024, 1, 5, 0, 0));

        // Act
        Listing savedListing = listingService.save(newListing);

        // Assert
        verify(listingRepository, times(1)).save(listingCaptor.capture());
        Listing savedListingArgument = listingCaptor.getValue();
        assertEquals(newListing, savedListingArgument);
    }
}