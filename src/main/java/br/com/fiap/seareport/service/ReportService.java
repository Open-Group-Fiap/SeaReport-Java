package br.com.fiap.seareport.service;

import br.com.fiap.seareport.dto.ServiceDTO;
import br.com.fiap.seareport.dto.request.ReportRequest;
import br.com.fiap.seareport.dto.response.ReportResponse;
import br.com.fiap.seareport.entity.Category;
import br.com.fiap.seareport.entity.Report;
import br.com.fiap.seareport.entity.User;
import br.com.fiap.seareport.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ReportService implements ServiceDTO<Report, ReportRequest, ReportResponse, Long> {
    @Autowired
    private ReportRepository repo;
    @Autowired
    private UserService userService;

    @Override
    public Report toEntity(ReportRequest r) {
        if (Objects.isNull(r) ) return null;
        var user = userService.findById(r.userId());
        // Mapeia o ID da categoria para a enumeração correspondente
        Category category = Category.fromId(r.category());
        return Report.builder()
                .dateReport(LocalDateTime.now())
                .description(r.description())
                .user(user)
                .location(r.location())
                .category(category)
                .approved(false)
                .build();
    }




    @Override
    public ReportResponse toResponse(Report e) {
        if (Objects.isNull(e)) return null;
        return ReportResponse.builder()
                .id(e.getId())
                .dateReport(e.getDateReport())
                .description(e.getDescription())
                .approved(e.getApproved())
                .location(e.getLocation())
                .category(e.getCategory().getName())
                .build();
    }

    @Override
    public List<Report> findAll() {
        return repo.findAll();
    }

    @Override
    public List<Report> findAll(Example<Report> example) {
        return repo.findAll(example);
    }

    @Override
    public Report findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Report save(ReportRequest r) {
        return repo.save(toEntity(r));
    }

    public List<Report> getReportsByUserId(Long userId) {
        return repo.findByUserId(userId);
    }

    public List<Report> getUnprocessedReports() {
        return repo.findByApprovedFalse();
    }

    public Report approve(Long id) {
        var report = repo.findById(id).orElse(null);
        if (Objects.isNull(report)) return null;
        var user = userService.findById(report.getUser().getId());
        user.setXp(user.getXp() + 100);
        userService.save(user);
        report.setApproved(true);
        repo.save(report);
        return report;
    }
}