package cinema.cinema_app.service.serviceImpl;

import cinema.cinema_app.dto.TicketDtoForUsers;
import cinema.cinema_app.entity.Movie;
import cinema.cinema_app.repo.MovieRepository;
import cinema.cinema_app.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void mailSender(TicketDtoForUsers ticket) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(ticket.getEmail());
            helper.setSubject("🎬 Sizin Biletiniz!!!");


            String html = buildHTMLTicket(ticket);
            helper.setText(html, true);

            mailSender.send(message);
            System.out.println("✅ Send to : " + ticket.getEmail());

        } catch (MessagingException e) {
            throw new RuntimeException("Xeta", e);
        }

    }
    @Override
    public String buildHTMLTicket (TicketDtoForUsers ticket) {
        return String.format("""
        <div style="max-width:500px; margin:0 auto; font-family:Arial; background:linear-gradient(135deg, #667eea, #764ba2); border-radius:20px; padding:20px; color:white;">
            <div style="text-align:center; font-size:28px; font-weight:bold;">🎬 Biletiniz 🍿</div>
            <div style="background:rgba(255,255,255,0.2); border-radius:15px; padding:15px; margin:10px 0;">
                <div><strong>🎥 Film:</strong> %s</div>
                <div><strong>🏢 Cinema:</strong> %s</div>
                <div><strong>🎭 Zal:</strong> %s</div>
                <div><strong>📅 Zaman:</strong> %s</div>
            </div>
            <div style="background:#ffd700; color:#333; border-radius:10px; padding:10px; text-align:center; font-size:24px; font-weight:bold;">
                🪑 %d sira / %d yer
            </div>
            <div style="background:rgba(255,255,255,0.2); border-radius:15px; padding:15px; margin:10px 0;">
                <div><strong>🔢 Nomre:</strong> #%s</div>
                <div><strong>👤 Yas 18-den boyukdu?:</strong> %s</div>
            </div>
            <div style="text-align:center; font-size:12px; margin-top:20px;">Bileti girisde gorsedin, Tessekurler</div>
        </div>
        """,
                ticket.getMovie(),
                ticket.getCinema(),
                ticket.getCinemaHall(),
                ticket.getShowTime() != null ? ticket.getShowTime().toString() : "—",
                ticket.getRow(),
                ticket.getSeatColumn(),
                ticket.getNumber(),
                ticket.getOverEighteen() ? "Beli" : "Xeyr"
        );
    }
}
