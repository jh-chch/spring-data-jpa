package com.spring.datajpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.spring.datajpa.entity.Team;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class TeamJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Team save(Team team) {
        em.persist(team);
        return team;
    }

    public void delete(Team team) {
        em.remove(team);
    }

    public List<Team> findAll() {
        return em.createQuery(
                "select t from Team t", Team.class).getResultList();
    }

    public Long count() {
        return em.createQuery(
                "select count(t) from Team t", Long.class).getSingleResult();
    }

    public Optional<Team> findById(Long id) {
        return Optional.ofNullable(em.find(Team.class, id));
    }
}
