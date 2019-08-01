package by.kolenchik.web.servlet;

import by.kolenchik.core.command.CommandExecutor;
import by.kolenchik.core.command.attachment.AddFileCommand;
import by.kolenchik.core.command.attachment.GetAttachmentCommand;
import by.kolenchik.core.command.contact.*;
import by.kolenchik.core.command.email.GetEmailTemplateCommand;
import by.kolenchik.core.command.email.SendEmailListCommand;
import by.kolenchik.core.service.ContactService;
import by.kolenchik.core.service.PageService;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/main/*", loadOnStartup = 1)
public class FrontControllerServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(FrontControllerServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {

        String pathInfo = req.getPathInfo();
        log.debug(pathInfo);
        String queryString = req.getQueryString();

        String pageParameter = req.getParameter("page");
        String sizeParameter = req.getParameter("size");
        String attachmentHeader = req.getHeader("attachment");

        Boolean isSearchRequest = false;
        Boolean isEmailRequest = false;
        Boolean isTemplateRequest = false;
        Boolean isDownloadAttachment = false;

        if (req.getHeader("template") != null) {
            isTemplateRequest = true;
        }

        if (attachmentHeader != null) {
            isDownloadAttachment = true;
        }

        if (queryString != null) {

            isSearchRequest = (queryString.contains("firstname") || queryString.contains("lastname") ||
                    queryString.contains("patronymic") || queryString.contains("birthday") ||
                    queryString.contains("sex") || queryString.contains("maritalStatus") ||
                    queryString.contains("citizenship") || queryString.contains("country") ||
                    queryString.contains("city") || queryString.contains("street") ||
                    queryString.contains("building") || queryString.contains("apartment") ||
                    queryString.contains("index"));

            isEmailRequest = (queryString.contains("id"));
        }

        if (isSearchRequest) {

            log.info("Search request with query string = " + queryString);
            SearchContactsCommand searchContactsCOmmand = new SearchContactsCommand(req, resp);
            CommandExecutor executor = new CommandExecutor();
            executor.executeOperation(searchContactsCOmmand);

        } else if (isEmailRequest) {

            log.info("Email request with query string = " + queryString);
            GetEmailsByUserIdCommand getEmailsByUserIdCommand = new GetEmailsByUserIdCommand(req, resp);
            CommandExecutor executor = new CommandExecutor();
            executor.executeOperation(getEmailsByUserIdCommand);

        } else if (isTemplateRequest) {

            log.info("Template request");
            GetEmailTemplateCommand getEmailTemplateCommand = new GetEmailTemplateCommand(req, resp);
            CommandExecutor executor = new CommandExecutor();
            executor.executeOperation(getEmailTemplateCommand);

        } else if (isDownloadAttachment) {

            log.info("Download attachment");
            GetAttachmentCommand getAttachmentCommand = new GetAttachmentCommand(req, resp);
            CommandExecutor commandExecutor = new CommandExecutor();
            commandExecutor.executeOperation(getAttachmentCommand);

        } else if (pathInfo != null) {

            if (pathInfo.equals("/home")) {

                try {
                    RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/page/contacts.html");

                    log.info("forward to main html page");

                    requestDispatcher.forward(req, resp);

                } catch (IOException e) {
                    log.warn(e.getMessage());
                    try {
                        resp.sendError(404, "Resource was not found");
                    } catch (IOException ex) {
                        log.warn(ex.getMessage());
                    }
                }
            } else if (pathInfo.equals("/contacts")) {

                CommandExecutor commandExecutor = new CommandExecutor();
                ContactService contactService = new ContactService();


                Integer pageNumber = null;
                Integer pageSize = null;

                if (pageParameter != null) {
                    try {
                        pageNumber = Integer.parseInt(pageParameter);
                        pageSize = 10;
                        if (pageNumber < 1) {
                            throw new NumberFormatException();
                        }
                        pageNumber--;

                    } catch (NumberFormatException e) {
                        log.error(e.getMessage());
                        try {
                            resp.sendError(400, "Check page number format!");
                        } catch (IOException ex) {
                            log.warn(ex.getMessage());
                        }
                    }
                } else {
                    pageNumber = 0;
                    pageSize = 10;
                }

                log.info("get + " + pageSize +
                        " contacts start with position " + pageNumber + " in db");

                PageService pageService = new PageService();
                pageService.setPageNumber(pageNumber);
                pageService.setPageSize(pageSize);

                GetContactsCommand getContactsComm =
                        new GetContactsCommand(pageService.getPageNumber(), pageService.getPageSize(), contactService, req, resp);
                commandExecutor.executeOperation(getContactsComm);

            } else if (pathInfo.contains("/contacts/")) {

                CommandExecutor commandExecutor = new CommandExecutor();
                String contactIdStr = pathInfo.substring(10);
                Long contactId;

                try {
                    contactId = Long.parseLong(contactIdStr);

                    log.info("get contact by id = " + contactId);

                    commandExecutor.executeOperation(
                            new GetContactByIdCommand(contactId, req, resp)
                    );
                } catch (NumberFormatException e) {
                    log.error(e.getMessage());
                    try {
                        resp.sendError(400, "Wrong request! Please, Check url!");
                    } catch (IOException ex) {
                        log.warn(ex.getMessage());
                    }
                }
            } else {
                try {
                    resp.sendError(404, "Resource wasn't found");
                } catch (IOException e) {
                    log.warn(e.getMessage());
                }
            }
        } else {
            try {
                resp.sendError(404, "Resource wasn't found");
            } catch (IOException e) {
                log.warn(e.getMessage());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {

        CommandExecutor executor = new CommandExecutor();

        Boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        Boolean isEmail = false;
        String emailHeader = req.getHeader("email");

        if (emailHeader != null) {
            isEmail = true;
        }


        if (isMultipart) {
            AddFileCommand addFileCommand = new AddFileCommand(req, resp);
            executor.executeOperation(addFileCommand);

        } else {

            String servletPath = req.getServletPath();
            String pathInfo = req.getPathInfo();
            String queryString = req.getQueryString();

            if (isEmail) {

                log.info("send email");

                SendEmailListCommand sendEmailListCommand = new SendEmailListCommand(req, resp);
                executor.executeOperation(sendEmailListCommand);

            } else if (pathInfo.equals("/contacts")) {

                CreateContactCommand createContactCommand = new CreateContactCommand(req, resp);
                executor.executeOperation(createContactCommand);

            } else {
                try {
                    resp.sendError(404, "Wrong request! Please, Check url!");
                } catch (IOException e) {
                    log.warn(e.getMessage());
                }
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException {

        String pathInfo = req.getPathInfo();
        log.debug(pathInfo);

        Long contactId = null;

        CommandExecutor commandExecutor = new CommandExecutor();

        if (pathInfo.contains("/contacts/")) {

            try {
                String contactIdStr = pathInfo.substring(10);

                contactId = Long.parseLong(contactIdStr);

                ContactService contactService = new ContactService();

                UpdateContactCommand command = new UpdateContactCommand(contactService, contactId, req, resp);
                commandExecutor.executeOperation(command);

            } catch (NumberFormatException e) {
                log.error(e.getMessage());
                try {
                    resp.sendError(400, "Wrong request! Please, Check url!");
                } catch (IOException ex) {
                    log.warn(e.getMessage());
                }
            }
        } else {
            try {
                resp.sendError(404, "Resource wasn't found");
            } catch (IOException e) {
                log.warn(e.getMessage());
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException {

        String pathInfo = req.getPathInfo();

        CommandExecutor commandExecutor = new CommandExecutor();

        Long contactId = null;

        if (pathInfo.contains("/contacts/")){

            try {
                String contactIdStr = pathInfo.substring(10);

                contactId = Long.parseLong(contactIdStr);

                log.info("Delete contact with id=" + contactId);

                ContactService contactService = new ContactService();

                commandExecutor.executeOperation(new DeleteContactCommand(contactService, contactId, req, resp));

            } catch (NumberFormatException e) {
                log.error(e.getMessage());
                try {
                    resp.sendError(400, "Wrong request! Please, Check url!");
                } catch (IOException ex) {
                    log.warn(ex.getMessage());
                }
            }

        } else {
            try {
                resp.sendError(404, "Resource wasn't found");
            } catch (IOException e) {
                log.warn(e.getMessage());
            }
        }
    }
}
