package com.fastshop.web.rest;

import com.fastshop.repository.ColorRepository;
import com.fastshop.service.ColorService;
import com.fastshop.service.dto.ColorDTO;
import com.fastshop.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.fastshop.domain.Color}.
 */
@RestController
@RequestMapping("/api")
public class ColorResource {

    private final Logger log = LoggerFactory.getLogger(ColorResource.class);

    private static final String ENTITY_NAME = "color";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ColorService colorService;

    private final ColorRepository colorRepository;

    public ColorResource(ColorService colorService, ColorRepository colorRepository) {
        this.colorService = colorService;
        this.colorRepository = colorRepository;
    }

    /**
     * {@code POST  /colors} : Create a new color.
     *
     * @param colorDTO the colorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new colorDTO, or with status {@code 400 (Bad Request)} if the color has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/colors")
    public ResponseEntity<ColorDTO> createColor(@Valid @RequestBody ColorDTO colorDTO) throws URISyntaxException {
        log.debug("REST request to save Color : {}", colorDTO);
        if (colorDTO.getId() != null) {
            throw new BadRequestAlertException("A new color cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ColorDTO result = colorService.save(colorDTO);
        return ResponseEntity
            .created(new URI("/api/colors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /colors/:id} : Updates an existing color.
     *
     * @param id the id of the colorDTO to save.
     * @param colorDTO the colorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated colorDTO,
     * or with status {@code 400 (Bad Request)} if the colorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the colorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/colors/{id}")
    public ResponseEntity<ColorDTO> updateColor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ColorDTO colorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Color : {}, {}", id, colorDTO);
        if (colorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, colorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!colorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ColorDTO result = colorService.update(colorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, colorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /colors/:id} : Partial updates given fields of an existing color, field will ignore if it is null
     *
     * @param id the id of the colorDTO to save.
     * @param colorDTO the colorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated colorDTO,
     * or with status {@code 400 (Bad Request)} if the colorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the colorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the colorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/colors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ColorDTO> partialUpdateColor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ColorDTO colorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Color partially : {}, {}", id, colorDTO);
        if (colorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, colorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!colorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ColorDTO> result = colorService.partialUpdate(colorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, colorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /colors} : get all the colors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of colors in body.
     */
    @GetMapping("/colors")
    public ResponseEntity<List<ColorDTO>> getAllColors(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Colors");
        Page<ColorDTO> page = colorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /colors/:id} : get the "id" color.
     *
     * @param id the id of the colorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the colorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/colors/{id}")
    public ResponseEntity<ColorDTO> getColor(@PathVariable Long id) {
        log.debug("REST request to get Color : {}", id);
        Optional<ColorDTO> colorDTO = colorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(colorDTO);
    }

    /**
     * {@code DELETE  /colors/:id} : delete the "id" color.
     *
     * @param id the id of the colorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/colors/{id}")
    public ResponseEntity<Void> deleteColor(@PathVariable Long id) {
        log.debug("REST request to delete Color : {}", id);
        colorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
