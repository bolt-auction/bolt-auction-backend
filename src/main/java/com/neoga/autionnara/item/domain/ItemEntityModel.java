package com.neoga.autionnara.item.domain;

import com.neoga.autionnara.item.controller.ItemController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ItemEntityModel extends EntityModel {

    public ItemEntityModel(ItemDto itemDto, Link... links) {
        super(itemDto, links);
        add(linkTo(ItemController.class).slash(itemDto.getItemId()).withSelfRel());
    }
}
