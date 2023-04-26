package com.boot.jx.mongo.logger;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import com.boot.jx.mongo.CommonDocInterfaces.IDocument;

@Document(collection = "CHANGE_LOG")
@TypeAlias("ChangeLogDoc")
public class ChangeLogDoc implements IDocument {

    @Id
    private String id;

}
