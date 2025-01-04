package com.kma.engfinity.repository;

import com.kma.engfinity.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, String> {
}
